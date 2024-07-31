package com.github.nxkoo.nxgeckolib.core.client.render.entity.layer;

import com.github.nxkoo.nxgeckolib.core.client.model.tools.geckolib.NxGeoBone;
import com.github.nxkoo.nxgeckolib.core.client.render.entity.player.GeckoRenderPlayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.ParrotVariantLayer;

public class GeckoParrotVariantLayer extends ParrotVariantLayer<AbstractClientPlayerEntity> implements IGeckoRenderLayer {
    private final GeckoRenderPlayer renderPlayerAnimated;

    public GeckoParrotVariantLayer(GeckoRenderPlayer rendererIn) {
        super(rendererIn);
        renderPlayerAnimated = rendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        try {
            NxGeoBone bone = renderPlayerAnimated.getAnimatedPlayerModel().getNxBone("Body");
            MatrixStack newMatrixStack = new MatrixStack();
            newMatrixStack.last().normal().mul(bone.getWorldSpaceNormal());
            newMatrixStack.last().pose().multiply(bone.getWorldSpaceXform());

            this.render(newMatrixStack, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, true);
            this.render(newMatrixStack, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, false);
        }
        catch (RuntimeException ignored) {}
    }
}
