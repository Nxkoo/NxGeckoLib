package com.github.nxkoo.nxgeckolib.core.client.render.entity.layer;

import com.github.nxkoo.nxgeckolib.core.client.model.tools.geckolib.NxGeoBone;
import com.github.nxkoo.nxgeckolib.core.client.render.NxRenderUtils;
import com.github.nxkoo.nxgeckolib.core.client.render.entity.player.GeckoRenderPlayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeckoHeldItemLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> implements IGeckoRenderLayer {
    private GeckoRenderPlayer renderPlayerAnimated;

    public GeckoHeldItemLayer(GeckoRenderPlayer entityRendererIn) {
        super(entityRendererIn);
        renderPlayerAnimated = entityRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!renderPlayerAnimated.getAnimatedPlayerModel().isInitialized()) return;
        boolean flag = entitylivingbaseIn.getMainArm() == HandSide.RIGHT;
        ItemStack mainHandStack = entitylivingbaseIn.getMainHandItem();
        ItemStack offHandStack = entitylivingbaseIn.getOffhandItem();

        ItemStack itemstack = flag ? offHandStack : mainHandStack;
        ItemStack itemstack1 = flag ? mainHandStack : offHandStack;
        if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
            matrixStackIn.pushPose();
            if (this.getParentModel().young) {
                float f = 0.5F;
                matrixStackIn.translate(0.0D, 0.75D, 0.0D);
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            }

            this.func_229135_a_(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);
            this.func_229135_a_(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
        }
    }

    private void func_229135_a_(LivingEntity entity, ItemStack itemStack, ItemCameraTransforms.TransformType transformType, HandSide side, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn) {
        if (!itemStack.isEmpty()) {
            String boneName = side == HandSide.RIGHT ? "RightHeldItem" : "LeftHeldItem";
            NxGeoBone bone = renderPlayerAnimated.getAnimatedPlayerModel().getNxBone(boneName);
            MatrixStack newMatrixStack = new MatrixStack();
            newMatrixStack.last().normal().mul(bone.getWorldSpaceNormal());
            newMatrixStack.last().pose().multiply(bone.getWorldSpaceXform());
            NxRenderUtils.rotate(newMatrixStack, Vector3f.XP.rotationDegrees(-90.0F));
            boolean flag = side == HandSide.LEFT;
            Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, itemStack, transformType, flag, newMatrixStack, buffer, packedLightIn);
        }
    }
}
