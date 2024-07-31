package com.github.nxkoo.nxgeckolib.core.client.model.tools.geckolib;

import com.github.nxkoo.nxgeckolib.core.server.entity.IAnimationTickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.Collections;

public abstract class NxAnimatedGeoModel<T extends IAnimatable & IAnimationTickable> extends AnimatedGeoModel<T> {
    public NxAnimatedGeoModel() {
    }

    public NxGeoBone getNxBone(String boneName) {
        IBone bone = this.getBone(boneName);
        return (NxGeoBone) bone;
    }

    public boolean isInitialized() {
        return !this.getAnimationProcessor().getModelRendererList().isEmpty();
    }

    @Override
    public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
        Minecraft mc = Minecraft.getInstance();
        AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
        AnimationEvent<T> predicate;
        double currentTick = animatable.tickTimer();

        if (manager.startTick == -1)
            manager.startTick = currentTick + mc.getFrameTime();

        if (!Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused) {
            manager.tick = currentTick + mc.getFrameTime();
            double gameTick = manager.tick;
            double deltaTicks = gameTick - this.lastGameTickTime;
            this.seekTime += deltaTicks;
            this.lastGameTickTime = gameTick;
        }

        predicate = animationEvent == null ? new AnimationEvent<T>(animatable, 0, 0, (float)(manager.tick - this.lastGameTickTime), false, Collections.emptyList()) : animationEvent;
        predicate.animationTick = this.seekTime;

        getAnimationProcessor().preAnimationSetup(predicate.getAnimatable(), this.seekTime);

        if (!getAnimationProcessor().getModelRendererList().isEmpty())
            getAnimationProcessor().tickAnimation(animatable, instanceId, this.seekTime, predicate, GeckoLibCache.getInstance().parser, this.shouldCrashOnMissing);

        if (!Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused) {
            codeAnimations(animatable, instanceId, animationEvent);
        }
    }

    public void codeAnimations(T entity, Integer uniqueID, AnimationEvent<?> customPredicate) {

    }

    public boolean resourceForModelId(AbstractClientPlayerEntity player) {
        return true;
    }

    public float getControllerValue(String controllerName) {
        if (!isInitialized()) return 1.0f;
        return 1.0f - getBone(controllerName).getPositionX();
    }
}
