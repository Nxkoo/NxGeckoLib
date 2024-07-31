package com.github.nxkoo.nxgeckolib.proxy;

import com.github.nxkoo.nxgeckolib.proxy.handlers.ClientEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(IEventBus modbus) {
        super.init(modbus);
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
    }
}
