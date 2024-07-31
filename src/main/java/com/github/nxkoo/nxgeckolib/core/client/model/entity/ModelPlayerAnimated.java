package com.github.nxkoo.nxgeckolib.core.client.model.entity;

import com.github.nxkoo.nxgeckolib.core.client.model.armor.NxElytraModel;
import com.github.nxkoo.nxgeckolib.core.client.model.tools.ModelRendererMatrix;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ModelPlayerAnimated<T extends LivingEntity> extends PlayerModel<T> {
    private final List<ModelRenderer> modelRenderers = Lists.newArrayList();

    public ModelRenderer nxJacket = this.jacket;
    public ModelRenderer nxLeftSleeve = this.leftSleeve;
    public ModelRenderer nxRightSleeve = this.rightSleeve;
    public ModelRenderer nxLeftPants = this.leftPants;
    public ModelRenderer nxRightPants = this.rightPants;

    public ModelPlayerAnimated(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
        this.body = new ModelRendererMatrix(body);
        this.head = new ModelRendererMatrix(head);
        this.rightArm = new ModelRendererMatrix(rightArm);
        this.leftArm = new ModelRendererMatrix(leftArm);
        this.rightLeg = new ModelRendererMatrix(rightLeg);
        this.leftLeg = new ModelRendererMatrix(leftLeg);

        this.hat = new ModelRendererMatrix(hat);
        this.nxJacket = new ModelRendererMatrix(nxJacket);
        this.nxLeftSleeve = new ModelRendererMatrix(nxLeftSleeve);
        this.nxRightSleeve = new ModelRendererMatrix(nxRightSleeve);
        this.nxLeftPants = new ModelRendererMatrix(nxLeftPants);
        this.nxRightPants = new ModelRendererMatrix(nxRightPants);

        if (smallArmsIn) {
            modelRenderers.add(leftArm);
            modelRenderers.add(rightArm);
            modelRenderers.add(leftSleeve);
            modelRenderers.add(rightSleeve);
        } else {
            modelRenderers.add(leftArm);
            modelRenderers.add(leftSleeve);
            modelRenderers.add(rightSleeve);
        }
        modelRenderers.add(leftLeg);
        modelRenderers.add(leftPants);
        modelRenderers.add(rightPants);
        modelRenderers.add(jacket);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.nxLeftPants.copyFrom(this.leftLeg);
        this.nxRightPants.copyFrom(this.rightLeg);
        this.nxLeftSleeve.copyFrom(this.leftArm);
        this.nxRightSleeve.copyFrom(this.rightArm);
        this.nxJacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }

    @Override
    public ModelRenderer getRandomModelPart(Random randomIn) {
        return this.modelRenderers.get(randomIn.nextInt(this.modelRenderers.size()));
    }

    @Override
    public void copyPropertiesTo(EntityModel<T> model) {
        super.copyPropertiesTo(model);
        if (model instanceof NxElytraModel) {
            NxElytraModel<?> elytraModel = (NxElytraModel<?>) model;
            elytraModel.bipedBody.copyFrom(this.body);
        }
    }

    @Override
    public void copyPropertiesTo(BipedModel<T> modelIn) {
        if (!(modelIn.body instanceof ModelRendererMatrix)) {
            modelIn.head = new ModelRendererMatrix(modelIn.head);
            modelIn.hat = new ModelRendererMatrix(modelIn.hat);
            modelIn.body = new ModelRendererMatrix(modelIn.body);
            modelIn.leftArm = new ModelRendererMatrix(modelIn.leftArm);
            modelIn.rightArm = new ModelRendererMatrix(modelIn.rightArm);
            modelIn.leftLeg = new ModelRendererMatrix(modelIn.leftLeg);
            modelIn.rightLeg = new ModelRendererMatrix(modelIn.rightLeg);
        }
        setUseMatrixMode(modelIn, true);
        super.copyPropertiesTo(modelIn);
    }

    public static void setUseMatrixMode(BipedModel<? extends LivingEntity> bipedModel, boolean useMatrixMode) {
        ModelBipedAnimated.setUseMatrixMode(bipedModel, useMatrixMode);
    }
}
