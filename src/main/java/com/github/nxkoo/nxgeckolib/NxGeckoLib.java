package com.github.nxkoo.nxgeckolib;

import com.github.nxkoo.nxgeckolib.core.client.model.tools.NxGeoBuilder;
import com.github.nxkoo.nxgeckolib.core.server.ability.AbilityCommonEventHandler;
import com.github.nxkoo.nxgeckolib.core.server.capability.CapabilityHandler;
import com.github.nxkoo.nxgeckolib.proxy.ClientProxy;
import com.github.nxkoo.nxgeckolib.proxy.CommonProxy;
import com.github.nxkoo.nxgeckolib.proxy.handlers.ServerEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

import java.util.stream.Collectors;

@Mod(NxGeckoLib.MODID)
public class NxGeckoLib {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "nxgeckolib";
    public static CommonProxy PROXY;

    public static SimpleChannel NETWORK;

    public NxGeckoLib() {
        GeckoLibMod.DISABLE_IN_DEV = true;
        NxGeoBuilder.registerGeoBuilder(MODID, new NxGeoBuilder());
        GeckoLib.initialize();

        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();


        PROXY.init(bus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        MinecraftForge.EVENT_BUS.register(new AbilityCommonEventHandler());
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("NxGeckoLib is started...");
        CapabilityHandler.register();
        PROXY.initNetwork();
    }


    private void doClientStuff(final FMLClientSetupEvent event) {}

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("nxgeckolib", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("NxGeckoLib on server started...");
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {}
}
