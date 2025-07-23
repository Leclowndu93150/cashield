package com.leclowndu93150.shield.renderer;

import com.leclowndu93150.shield.item.CaptainAmericaShieldItem;
import com.leclowndu93150.shield.model.CaptainAmericaShieldModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class CaptainAmericaShieldRenderer extends GeoItemRenderer<CaptainAmericaShieldItem> {

    public CaptainAmericaShieldRenderer() {
        super(new CaptainAmericaShieldModel());
    }

    @Override
    public RenderType getRenderType(CaptainAmericaShieldItem animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder,
                                    int packedLightIn, net.minecraft.resources.ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void renderByItem(net.minecraft.world.item.ItemStack stack, ItemTransforms.TransformType transformType,
                             PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        poseStack.pushPose();

        switch (transformType) {
            case FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(5));
                poseStack.translate(-0.1, -0.3, 0.4);
                poseStack.scale(1.2f, 1.2f, 1.2f);
            }
            case FIRST_PERSON_RIGHT_HAND -> {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(5));
                poseStack.translate(0.1, -0.3, 0.4);
                poseStack.scale(1.2f, 1.2f, 1.2f);
            }
            case THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(5));
                poseStack.translate(0.1, -0.1, 0.15);
                poseStack.scale(1.0f, 1.0f, 1.0f);
            }
            case THIRD_PERSON_RIGHT_HAND -> {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(-5));
                poseStack.translate(-0.1, -0.1, 0.15);
                poseStack.scale(1.0f, 1.0f, 1.0f);
            }
            case GUI -> {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(30));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(45));
                poseStack.scale(0.625f, 0.625f, 0.625f);
            }
            case GROUND -> {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            case FIXED -> {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            }
        }

        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.popPose();
    }
}