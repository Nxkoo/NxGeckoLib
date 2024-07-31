package com.github.nxkoo.nxgeckolib.core.client.render.entity.layer;

import com.github.nxkoo.nxgeckolib.core.client.model.entity.ModelBipedAnimated;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GeckoArmorLayer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends BipedArmorLayer<T, M, A> {
    public GeckoArmorLayer(IEntityRenderer<T, M> renderer, A innerModel, A outerModel) {
        super(renderer, innerModel, outerModel);
    }

    public void render(MatrixStack stack, IRenderTypeBuffer buffer, T entity, EquipmentSlotType slot, int p_241739_5_, A model) {
        ItemStack itemstack = entity.getItemBySlot(slot);
        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem) itemstack.getItem();
            if (armoritem.getEquipmentSlot(itemstack) == slot) {
                model = getArmorModelHook(entity, itemstack, slot, model);
                this.getParentModel().copyPropertiesTo(model);
                this.setPartVisibility(model, slot);
                boolean flag = this.isLegSlot(slot);
                boolean flag1 = itemstack.hasFoil();
                if (armoritem instanceof net.minecraft.item.IDyeableArmorItem) {
                    int i = ((net.minecraft.item.IDyeableArmorItem) armoritem).getColor(itemstack);
                    float f = (float) (i >> 16 & 255) / 255.0F;
                    float f1 = (float) (i >> 8 & 255) / 255.0F;
                    float f2 = (float) (i & 255) / 255.0F;
                    this.renderModel(stack, buffer, p_241739_5_, flag1, model, f, f1, f2, this.getArmorResource(entity, itemstack, slot, null));
                    ModelBipedAnimated.setUseMatrixMode(model, true);
                    this.renderModel(stack, buffer, p_241739_5_, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, "overlay"));
                } else {
                    this.renderModel(stack, buffer, p_241739_5_, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slot, null));
                }
            }
        }
    }

    private void renderModel(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i, boolean b, A a, float f, float v, float v1, ResourceLocation armorResource) {
        IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(armorResource), false, b);
        a.renderToBuffer(matrixStack, vertexBuilder, i, OverlayTexture.NO_OVERLAY, f, v, v1, 1.0F);
    }

    private boolean isLegSlot(EquipmentSlotType slotIn) {
        return slotIn == EquipmentSlotType.LEGS;
    }
}
