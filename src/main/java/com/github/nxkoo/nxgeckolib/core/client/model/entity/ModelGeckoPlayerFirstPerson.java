package com.github.nxkoo.nxgeckolib.core.client.model.entity;

import com.github.nxkoo.nxgeckolib.NxGeckoLib;
import com.github.nxkoo.nxgeckolib.core.client.model.tools.geckolib.NxAnimatedGeoModel;
import com.github.nxkoo.nxgeckolib.core.client.model.tools.geckolib.NxGeoBone;
import com.github.nxkoo.nxgeckolib.core.client.render.entity.player.GeckoPlayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelGeckoPlayerFirstPerson extends NxAnimatedGeoModel<GeckoPlayer> {
	
	private ResourceLocation animationFileLocation;
	private ResourceLocation modelLocation;
	private ResourceLocation textureLocation;

	public BipedModel.ArmPose leftArmPose = BipedModel.ArmPose.EMPTY;
	public BipedModel.ArmPose rightArmPose = BipedModel.ArmPose.EMPTY;

	protected boolean useSmallArms;
	
	@Override
	public ResourceLocation getAnimationFileLocation(GeckoPlayer animatable) {
		return animationFileLocation;
	}

	@Override
	public ResourceLocation getModelLocation(GeckoPlayer animatable) {
		return modelLocation;
	}

	@Override
	public ResourceLocation getTextureLocation(GeckoPlayer animatable) {
		return textureLocation;
	}

	public void setUseSmallArms(boolean useSmallArms) {
		this.useSmallArms = useSmallArms;
	}

	public boolean isUsingSmallArms() {
		return useSmallArms;
	}

	@Override
	public void setLivingAnimations(GeckoPlayer entity, Integer uniqueID) {
		super.setLivingAnimations(entity, uniqueID);
		if (isInitialized()) {
			NxGeoBone rightArmLayerClassic = getNxBone("RightArmLayerClassic");
			NxGeoBone leftArmLayerClassic = getNxBone("LeftArmLayerClassic");
			NxGeoBone rightArmLayerSlim = getNxBone("RightArmLayerSlim");
			NxGeoBone leftArmLayerSlim = getNxBone("LeftArmLayerSlim");
			NxGeoBone rightArmClassic = getNxBone("RightArmClassic");
			NxGeoBone leftArmClassic = getNxBone("LeftArmClassic");
			NxGeoBone rightArmSlim = getNxBone("RightArmSlim");
			NxGeoBone leftArmSlim = getNxBone("LeftArmSlim");
			getNxBone("LeftHeldItem").setHidden(true);
			getNxBone("RightHeldItem").setHidden(true);
			rightArmClassic.setHidden(true);
			leftArmClassic.setHidden(true);
			rightArmLayerClassic.setHidden(true);
			leftArmLayerClassic.setHidden(true);
			rightArmSlim.setHidden(true);
			leftArmSlim.setHidden(true);
			rightArmLayerSlim.setHidden(true);
			leftArmLayerSlim.setHidden(true);
		}
	}

	/** Check if the modelId has some ResourceLocation **/
	@Override
	public boolean resourceForModelId(AbstractClientPlayerEntity player) {
		this.animationFileLocation = new ResourceLocation(NxGeckoLib.MODID, "animations/animated_player_first_person.animation.json");
		this.modelLocation = new ResourceLocation(NxGeckoLib.MODID, "geo/animated_player_first_person.geo.json");
		this.textureLocation = player.getSkinTextureLocation();
		return true;
	}
}