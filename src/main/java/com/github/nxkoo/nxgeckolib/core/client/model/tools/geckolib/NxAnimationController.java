package com.github.nxkoo.nxgeckolib.core.client.model.tools.geckolib;

import com.github.nxkoo.nxgeckolib.core.server.entity.IAnimationTickable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;

public class NxAnimationController<T extends IAnimatable & IAnimationTickable> extends AnimationController<T> {
    private double tickOffset;

    public NxAnimationController(T animatable, String name, float transitionLengthTicks, IAnimationPredicate<T> animationPredicate) {
        super(animatable, name, transitionLengthTicks, animationPredicate);
        tickOffset = 0.0d;
    }

    public void playAnimation(T animatable, AnimationBuilder animationBuilder) {
        markNeedsReload();
        setAnimation(animationBuilder);
        currentAnimation = this.animationQueue.poll();
        isJustStarting = true;
        adjustTick(animatable.tickTimer());
        transitionLengthTicks = 0;
    }

    @Override
    protected double adjustTick(double tick) {
        if (this.shouldResetTick) {
            if (getAnimationState() == AnimationState.Transitioning) {
                this.tickOffset = tick;
            }
            else if (getAnimationState() == AnimationState.Running) {
                this.tickOffset += transitionLengthTicks;
            }
            this.shouldResetTick = false;
        }
        return Math.max(tick - this.tickOffset, 0.0D);
    }
}
