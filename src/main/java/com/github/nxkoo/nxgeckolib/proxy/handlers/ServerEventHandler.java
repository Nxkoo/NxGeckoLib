package com.github.nxkoo.nxgeckolib.proxy.handlers;

import com.github.nxkoo.nxgeckolib.NxGeckoLib;
import com.github.nxkoo.nxgeckolib.core.server.capability.CapabilityHandler;
import com.github.nxkoo.nxgeckolib.core.server.capability.PlayerCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class ServerEventHandler {
    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability((PlayerEntity) event.getEntity(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) playerCapability.addedToWorld(event);
        }

        if (event.getWorld().isClientSide) {
            return;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player == null) {
            return;
        }
        PlayerEntity player = event.player;
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            playerCapability.tick(event);
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(NxGeckoLib.MODID, "player"), new PlayerCapability.PlayerProvider());
        }
    }
}
