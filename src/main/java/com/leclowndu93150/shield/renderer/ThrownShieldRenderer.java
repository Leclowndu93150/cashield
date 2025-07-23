package com.leclowndu93150.shield.renderer;

import com.leclowndu93150.shield.Shield;
import com.leclowndu93150.shield.entity.ThrownShield;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrownShieldRenderer extends EntityRenderer<ThrownShield> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Shield.MODID, "textures/item/captain_america_shield.png");
    private final ItemRenderer itemRenderer;

    public ThrownShieldRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ThrownShield entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Vector3f.YP.rotationDegrees(yRot - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(xRot));

        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        float spin = (entity.tickCount + partialTicks) * 20.0F;
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(spin));

        poseStack.scale(1.5F, 1.5F, 1.5F);

        ItemStack shieldStack = new ItemStack(Shield.CAPTAIN_AMERICA_SHIELD.get());
        this.itemRenderer.renderStatic(shieldStack, ItemTransforms.TransformType.GROUND,
                packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, (int)entity.getId());

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownShield entity) {
        return TEXTURE;
    }
}