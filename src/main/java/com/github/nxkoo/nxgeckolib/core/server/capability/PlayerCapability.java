package com.github.nxkoo.nxgeckolib.core.server.capability;

import com.github.nxkoo.nxgeckolib.core.client.render.entity.player.GeckoFirstPersonRenderer;
import com.github.nxkoo.nxgeckolib.core.client.render.entity.player.GeckoPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerCapability {

    public interface IPlayerCapability {
        INBT writeNBT();

        void readNBT(INBT nbt);

        void tick(TickEvent.PlayerTickEvent event);

        void addedToWorld(EntityJoinWorldEvent event);

        boolean isVerticalSwing();

        void setVerticalSwing(boolean verticalSwing);

        boolean isMouseRightDown();

        void setMouseRightDown(boolean mouseRightDown);

        boolean isMouseLeftDown();

        void setMouseLeftDown(boolean mouseLeftDown);

        boolean isPrevSneaking();

        void setPrevSneaking(boolean prevSneaking);

        Vector3d getPrevMotion();

        float getPrevCooledAttackStrength();

        void setPrevCooledAttackStrength(float cooledAttackStrength);

        @OnlyIn(Dist.CLIENT)
        GeckoPlayer.GeckoPlayerThirdPerson getGeckoPlayer();
    }

    public static class PlayerCapabilityImp implements IPlayerCapability {
        public boolean verticalSwing = false;
        private int prevTime;
        private int time;
        public boolean mouseRightDown = false;
        public boolean mouseLeftDown = false;
        public boolean prevSneaking;
        private float prevCooledAttackStrength;

        @OnlyIn(Dist.CLIENT)
        private GeckoPlayer.GeckoPlayerThirdPerson geckoPlayer;

        public boolean isVerticalSwing() {
            return verticalSwing;
        }

        public void setVerticalSwing(boolean verticalSwing) {
            this.verticalSwing = verticalSwing;
        }

        public boolean isMouseRightDown() {
            return mouseRightDown;
        }

        public void setMouseRightDown(boolean mouseRightDown) {
            this.mouseRightDown = mouseRightDown;
        }

        public boolean isMouseLeftDown() {
            return mouseLeftDown;
        }

        public void setMouseLeftDown(boolean mouseLeftDown) {
            this.mouseLeftDown = mouseLeftDown;
        }

        public boolean isPrevSneaking() {
            return prevSneaking;
        }

        public void setPrevSneaking(boolean prevSneaking) {
            this.prevSneaking = prevSneaking;
        }

        public Vector3d getPrevMotion() {
            return prevMotion;
        }

        @Override
        public float getPrevCooledAttackStrength() {
            return prevCooledAttackStrength;
        }

        @Override
        public void setPrevCooledAttackStrength(float cooledAttackStrength) {
            prevCooledAttackStrength = cooledAttackStrength;
        }

        @OnlyIn(Dist.CLIENT)
        public GeckoPlayer.GeckoPlayerThirdPerson getGeckoPlayer() {
            return geckoPlayer;
        }

        public Vector3d prevMotion;

        @Override
        public void addedToWorld(EntityJoinWorldEvent event) {
            if (event.getWorld().isClientSide()) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                geckoPlayer = new GeckoPlayer.GeckoPlayerThirdPerson(player);
                if (event.getEntity() == Minecraft.getInstance().player) GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON = new GeckoPlayer.GeckoPlayerFirstPerson(player);
            }
        }

        public void tick(TickEvent.PlayerTickEvent event) {
            PlayerEntity player = event.player;

            prevMotion = player.position().subtract(new Vector3d(player.xo, player.yo, player.zo));
            prevTime = time;
        }


        public int getTick() {
            return time;
        }

        public void decrementTime() {
            time--;
        }

        @Override
        public INBT writeNBT() {
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("prevTime", prevTime);
            compound.putInt("time", time);
            return compound;
        }

        @Override
        public void readNBT(INBT nbt) {
            CompoundNBT compound = (CompoundNBT) nbt;
            prevTime = compound.getInt("prevTime");
            time = compound.getInt("time");
        }
    }

    public static class PlayerStorage implements Capability.IStorage<IPlayerCapability> {
        @Override
        public INBT writeNBT(Capability<IPlayerCapability> capability, IPlayerCapability instance, Direction side) {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerCapability> capability, IPlayerCapability instance, Direction side, INBT nbt) {
            instance.readNBT(nbt);
        }
    }

    public static class PlayerProvider implements ICapabilitySerializable<INBT>
    {
        @CapabilityInject(IPlayerCapability.class)
        public static final Capability<IPlayerCapability> PLAYER_CAPABILITY = null;

        private final LazyOptional<IPlayerCapability> instance = LazyOptional.of(PLAYER_CAPABILITY::getDefaultInstance);

        @Override
        public INBT serializeNBT() {
            return PLAYER_CAPABILITY.getStorage().writeNBT(PLAYER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null);
        }

        @Override
        public void deserializeNBT(INBT nbt) {
            PLAYER_CAPABILITY.getStorage().readNBT(PLAYER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null, nbt);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == PLAYER_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }
}
