package com.leclowndu93150.shield.client;

import com.leclowndu93150.shield.Shield;
import com.leclowndu93150.shield.network.PacketHandler;
import com.leclowndu93150.shield.network.StartThrowPacket;
import com.leclowndu93150.shield.network.ReleaseThrowPacket;
import com.leclowndu93150.shield.item.CaptainAmericaShieldItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shield.MODID, value = Dist.CLIENT)
public class ClientEvents {
    private static boolean wasThrowKeyDown = false;
    private static int chargeStartTime = 0;
    private static boolean isCharging = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                boolean isThrowKeyDown = KeyBindings.throwShieldKey.isDown();
                
                // Check if player has shield
                ItemStack mainHand = mc.player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack offHand = mc.player.getItemInHand(InteractionHand.OFF_HAND);
                boolean hasShield = mainHand.getItem() instanceof CaptainAmericaShieldItem || 
                                   offHand.getItem() instanceof CaptainAmericaShieldItem;
                
                if (hasShield && isThrowKeyDown != wasThrowKeyDown) {
                    if (isThrowKeyDown) {
                        // Key pressed - start charging
                        PacketHandler.INSTANCE.sendToServer(new StartThrowPacket());
                        chargeStartTime = mc.player.tickCount;
                        isCharging = true;
                    } else if (isCharging) {
                        // Key released - throw shield
                        PacketHandler.INSTANCE.sendToServer(new ReleaseThrowPacket());
                        isCharging = false;
                    }
                    wasThrowKeyDown = isThrowKeyDown;
                }
                
                // Reset if no shield
                if (!hasShield && isCharging) {
                    isCharging = false;
                    wasThrowKeyDown = false;
                }
            }
        }
    }
    
    public static boolean isChargingThrow() {
        return isCharging;
    }
    
    public static int getChargeTime() {
        if (!isCharging) return 0;
        return Minecraft.getInstance().player.tickCount - chargeStartTime;
    }
}