package com.leclowndu93150.shield.model;

import com.leclowndu93150.shield.Shield;
import com.leclowndu93150.shield.item.CaptainAmericaShieldItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CaptainAmericaShieldModel extends AnimatedGeoModel<CaptainAmericaShieldItem> {

    @Override
    public ResourceLocation getModelResource(CaptainAmericaShieldItem object) {
        return new ResourceLocation(Shield.MODID, "geo/cashield.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CaptainAmericaShieldItem object) {
        return new ResourceLocation(Shield.MODID, "textures/item/cashield.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CaptainAmericaShieldItem animatable) {
        return new ResourceLocation(Shield.MODID, "animations/cashield.animation.json");
    }
}