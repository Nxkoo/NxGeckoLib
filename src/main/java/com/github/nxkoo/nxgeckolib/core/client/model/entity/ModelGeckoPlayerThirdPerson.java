package com.github.nxkoo.nxgeckolib.core.client.model.entity;

import com.github.nxkoo.nxgeckolib.core.client.model.tools.geckolib.NxGeoBone;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelGeckoPlayerThirdPerson extends ModelGeckoBiped {

	public NxGeoBone bipedLeftArmwear() {
		return getNxBone("LeftArmLayer");
	}

	public NxGeoBone bipedRightArmwear() {
		return getNxBone("RightArmLayer");
	}

	public NxGeoBone bipedLeftLegwear() {
		return getNxBone("LeftLegLayer");
	}

	public NxGeoBone bipedRightLegwear() {
		return getNxBone("RightLegLayer");
	}

	public NxGeoBone bipedBodywear() {
		return getNxBone("BodyLayer");
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.bipedLeftArmwear().setHidden(!visible);
		this.bipedRightArmwear().setHidden(!visible);
		this.bipedLeftLegwear().setHidden(!visible);
		this.bipedRightLegwear().setHidden(!visible);
		this.bipedBodywear().setHidden(!visible);
	}

	@Override
	public void setRotationAngles(PlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTick) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTick);
		NxGeoBone rightArmLayerClassic = getNxBone("RightArmLayerClassic");
		NxGeoBone leftArmLayerClassic = getNxBone("LeftArmLayerClassic");
		NxGeoBone rightArmLayerSlim = getNxBone("RightArmLayerSlim");
		NxGeoBone leftArmLayerSlim = getNxBone("LeftArmLayerSlim");
		if (useSmallArms) {
			rightArmLayerClassic.setHidden(true);
			leftArmLayerClassic.setHidden(true);
			rightArmLayerSlim.setHidden(false);
			leftArmLayerSlim.setHidden(false);
		}
		else {
			rightArmLayerSlim.setHidden(true);
			leftArmLayerSlim.setHidden(true);
			rightArmLayerClassic.setHidden(false);
			leftArmLayerClassic.setHidden(false);
		}
	}
}