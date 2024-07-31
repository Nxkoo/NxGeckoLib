package com.github.nxkoo.nxgeckolib.core.server.ability;

import com.github.nxkoo.nxgeckolib.NxGeckoLib;
import com.github.nxkoo.nxgeckolib.core.server.ability.abilities.SimpleAnimationAbility;
import com.github.nxkoo.nxgeckolib.core.server.capability.AbilityCapability;
import com.github.nxkoo.nxgeckolib.core.server.capability.CapabilityHandler;
import com.github.nxkoo.nxgeckolib.core.server.message.MessageInterruptAbility;
import com.github.nxkoo.nxgeckolib.core.server.message.MessagePlayerUseAbility;
import com.github.nxkoo.nxgeckolib.core.server.message.MessageUseAbility;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;

public enum AbilityHandler {
    INSTANCE;

//    public static final AbilityType<FireballAbility> FIREBALL_ABILITY = new AbilityType<>("fireball", FireballAbility::new);
//    public static final AbilityType<SunstrikeAbility> SUNSTRIKE_ABILITY = new AbilityType<>("sunstrike", SunstrikeAbility::new);
//    public static final AbilityType<SolarBeamAbility> SOLAR_BEAM_ABILITY = new AbilityType<>("solar_beam", SolarBeamAbility::new);
//    public static final AbilityType<WroughtAxeSwingAbility> WROUGHT_AXE_SWING_ABILITY = new AbilityType<>("wrought_axe_swing", WroughtAxeSwingAbility::new);
//    public static final AbilityType<WroughtAxeSlamAbility> WROUGHT_AXE_SLAM_ABILITY = new AbilityType<>("wrought_axe_slam", WroughtAxeSlamAbility::new);
//    public static final AbilityType<IceBreathAbility> ICE_BREATH_ABILITY = new AbilityType<>("ice_breath", IceBreathAbility::new);
//    public static final AbilityType<SpawnBoulderAbility> SPAWN_BOULDER_ABILITY = new AbilityType<>("spawn_boulder", SpawnBoulderAbility::new);
//    public static final AbilityType<TunnelingAbility> TUNNELING_ABILITY = new AbilityType<>("tunneling", TunnelingAbility::new);
//    public static final AbilityType<SimpleAnimationAbility> HIT_BOULDER_ABILITY = new AbilityType<>("hit_boulder", (type, player) ->
//            new SimpleAnimationAbility(type, player, "hit_boulder", 10, false, false)
//    );
    public static final AbilityType<SimpleAnimationAbility> BACKSTAB_ABILITY = new AbilityType<>("backstab", (type, player) ->
            new SimpleAnimationAbility(type, player, "backstab", 12, true, true)
    );
    public static final AbilityType<?>[] PLAYER_ABILITIES = new AbilityType[] {
//            SUNSTRIKE_ABILITY,
//            SOLAR_BEAM_ABILITY,
//            WROUGHT_AXE_SWING_ABILITY,
//            WROUGHT_AXE_SLAM_ABILITY,
//            ICE_BREATH_ABILITY,
//            SPAWN_BOULDER_ABILITY,
//            TUNNELING_ABILITY,
//            HIT_BOULDER_ABILITY,
            BACKSTAB_ABILITY
    };

    @Nullable
    public AbilityCapability.IAbilityCapability getAbilityCapability(LivingEntity entity) {
        return CapabilityHandler.getCapability(entity, AbilityCapability.AbilityProvider.ABILITY_CAPABILITY);
    }

    @Nullable
    public Ability getAbility(LivingEntity entity, AbilityType<?> abilityType) {
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            return abilityCapability.getAbilityMap().get(abilityType);
        }
        return null;
    }

    public <T extends LivingEntity> void sendAbilityMessage(T entity, AbilityType<?> abilityType) {
        if (entity.getCommandSenderWorld().isClientSide()) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.canUse()) {
                abilityCapability.activateAbility(entity, abilityType);
                NxGeckoLib.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageUseAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)));
            }
        }
    }

    public <T extends LivingEntity> void sendInterruptAbilityMessage(T entity, AbilityType<?> abilityType) {
        if (entity.getCommandSenderWorld().isClientSide()) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.interrupt();
                NxGeckoLib.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageInterruptAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)));
            }
        }
    }

    public <T extends PlayerEntity> void sendPlayerTryAbilityMessage(T entity, AbilityType<?> ability) {
        if (!(entity.getCommandSenderWorld().isClientSide() && entity instanceof ClientPlayerEntity)) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            NxGeckoLib.NETWORK.sendToServer(new MessagePlayerUseAbility(ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), ability)));
        }
    }
}