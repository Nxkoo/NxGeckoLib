package com.github.nxkoo.nxgeckolib.proxy;

import com.github.nxkoo.nxgeckolib.NxGeckoLib;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CommonProxy{
    private int nextMessageId;

    public void init(final IEventBus modbus) {}

    public void initNetwork() {
        final String version = "1";
        NxGeckoLib.NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(NxGeckoLib.MODID, "net"))
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version::equals)
                .serverAcceptedVersions(version::equals)
                .simpleChannel();
        }

    private <MSG> void registerMessage(final Class<MSG> clazz, final BiConsumer<MSG, PacketBuffer> encoder, final Function<PacketBuffer, MSG> decoder, final BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
        NxGeckoLib.NETWORK.messageBuilder(clazz, this.nextMessageId++)
                .encoder(encoder).decoder(decoder)
                .consumer(consumer)
                .add();
    }
}