package com.github.nxkoo.nxgeckolib.core.client.model.armor;

import com.github.nxkoo.nxgeckolib.core.client.model.tools.ModelRendererMatrix;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NxElytraModel<T extends LivingEntity> extends ElytraModel<T> {
    public ModelRendererMatrix bipedBody;

    public NxElytraModel(ModelRenderer bipedBody) {
        this.bipedBody = new ModelRendererMatrix(bipedBody);
        this.bipedBody.cubes.clear();
        this.bipedBody.addChild(rightWing);
        this.bipedBody.addChild(leftWing);
        rightWing.z = 2;
        leftWing.z = 2;
    }

    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.bipedBody);
    }
}