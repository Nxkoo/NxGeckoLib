package com.github.nxkoo.nxgeckolib.core.server.message;

import com.github.nxkoo.nxgeckolib.core.server.ability.Ability;
import com.github.nxkoo.nxgeckolib.core.server.ability.AbilityType;
import com.github.nxkoo.nxgeckolib.core.server.capability.AbilityCapability;
import com.github.nxkoo.nxgeckolib.core.server.capability.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessageInterruptAbility {
    private int entityID;
    private int index;

    public MessageInterruptAbility() {

    }

    public MessageInterruptAbility(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    public static void serialize(final MessageInterruptAbility message, final PacketBuffer buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.index);
    }

    public static MessageInterruptAbility deserialize(final PacketBuffer buf) {
        final MessageInterruptAbility message = new MessageInterruptAbility();
        message.entityID = buf.readVarInt();
        message.index = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageInterruptAbility, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageInterruptAbility message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity != null) {
                        AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, AbilityCapability.AbilityProvider.ABILITY_CAPABILITY);
                        if (abilityCapability != null) {
                            AbilityType<?> abilityType = abilityCapability.getAbilityTypesOnEntity(entity)[message.index];
                            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
                            if (instance.isUsing()) instance.interrupt();
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
