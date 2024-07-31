package com.github.nxkoo.nxgeckolib.core.server.message;

import com.github.nxkoo.nxgeckolib.core.server.capability.AbilityCapability;
import com.github.nxkoo.nxgeckolib.core.server.capability.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessageUseAbility {
    private int entityID;
    private int index;

    public MessageUseAbility() {

    }

    public MessageUseAbility(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    public static void serialize(final MessageUseAbility message, final PacketBuffer buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.index);
    }

    public static MessageUseAbility deserialize(final PacketBuffer buf) {
        final MessageUseAbility message = new MessageUseAbility();
        message.entityID = buf.readVarInt();
        message.index = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageUseAbility, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageUseAbility message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity != null) {
                        AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, AbilityCapability.AbilityProvider.ABILITY_CAPABILITY);
                        if (abilityCapability != null) {
                            abilityCapability.activateAbility(entity, abilityCapability.getAbilityTypesOnEntity(entity)[message.index]);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
