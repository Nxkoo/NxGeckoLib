package com.github.nxkoo.nxgeckolib.proxy.handlers;

import com.github.nxkoo.nxgeckolib.NxGeckoLib;
import com.github.nxkoo.nxgeckolib.core.client.render.entity.player.GeckoPlayer;
import com.github.nxkoo.nxgeckolib.core.server.ability.AbilityHandler;
import com.github.nxkoo.nxgeckolib.core.server.capability.CapabilityHandler;
import com.github.nxkoo.nxgeckolib.core.server.capability.PlayerCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;


public class ServerEventHandler {
    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability((PlayerEntity) event.getEntity(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null) playerCapability.addedToWorld(event);
        }

        if (event.getWorld().isClientSide()) {
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
    public void checkCritEvent(CriticalHitEvent event) {
        ItemStack weapon = event.getPlayer().getMainHandItem();
        PlayerEntity attacker = event.getPlayer();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getPlayer(), PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null && /*playerCapability.getPrevCooledAttackStrength() == 1.0f &&*/ !weapon.isEmpty() && event.getTarget() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getTarget();
//            if (weapon.getItem() instanceof Hand) {
            Vector3d lookDir = new Vector3d(target.getViewVector(1).x, 0, target.getViewVector(1).z).normalize();
            Vector3d vecBetween = new Vector3d(target.position().x - event.getPlayer().position().x, 0, target.position().z - event.getPlayer().position().z).normalize();
            double dot = lookDir.dot(vecBetween);
            AbilityHandler.INSTANCE.sendAbilityMessage(attacker, AbilityHandler.BACKSTAB_ABILITY);
            if (dot > 0.7) {
                event.setResult(Event.Result.ALLOW);
                event.setDamageModifier(15);
            }
//            }
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(NxGeckoLib.MODID, "player"), new PlayerCapability.PlayerProvider());
        }
    }
}
