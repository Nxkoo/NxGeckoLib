package com.github.nxkoo.nxgeckolib.core.server.capability;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public final class CapabilityHandler {
    public static void register() {
        CapabilityManager.INSTANCE.register(PlayerCapability.IPlayerCapability.class, new PlayerCapability.PlayerStorage(), PlayerCapability.PlayerCapabilityImp::new);
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        if (entity == null) return null;
        if (!entity.isAlive()) return null;
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }
}

