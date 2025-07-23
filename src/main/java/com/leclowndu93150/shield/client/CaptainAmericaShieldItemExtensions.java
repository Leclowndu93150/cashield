package com.leclowndu93150.shield.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class CaptainAmericaShieldItemExtensions implements IClientItemExtensions {

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProgress, float swingProgress) {
        // Apply spear animation when charging with keybind
        if (ClientEvents.isChargingThrow() && itemInHand.getItem() instanceof com.leclowndu93150.shield.item.CaptainAmericaShieldItem) {
            // Check which hand has the shield
            boolean isMainHand = player.getItemInHand(InteractionHand.MAIN_HAND) == itemInHand;
            boolean isOffHand = player.getItemInHand(InteractionHand.OFF_HAND) == itemInHand;
            
            if ((isMainHand && arm == player.getMainArm()) || (isOffHand && arm != player.getMainArm())) {
                boolean flag = arm == HumanoidArm.RIGHT;
                int k = flag ? 1 : -1;

                poseStack.translate((double)((float)k * 0.56F), (double)(-0.52F + equipProgress * -0.6F), (double)-0.72F);
                poseStack.translate((double)((float)k * -0.3F), (double)0.6F, (double)0.1F);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(-55.0F));
                poseStack.mulPose(Vector3f.YP.rotationDegrees((float)k * 35.3F));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees((float)k * -9.785F));

                poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));

                float f7 = (float)ClientEvents.getChargeTime() + partialTick;
                float f11 = f7 / 10.0F;
                if (f11 > 1.0F) {
                    f11 = 1.0F;
                }

                if (f11 > 0.1F) {
                    float f14 = (float)Math.sin((f7 - 0.1F) * 1.3F);
                    float f17 = f11 - 0.1F;
                    float f19 = f14 * f17;
                    poseStack.translate((double)(f19 * 0.0F), (double)(f19 * 0.004F), (double)(f19 * 0.0F));
                }

                poseStack.translate((double)0.0F, (double)0.0F, (double)(f11 * 0.2F));
                poseStack.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
                poseStack.mulPose(Vector3f.YN.rotationDegrees((float)k * 45.0F));
                
                return true;
            }
        }
        return false;
    }
}