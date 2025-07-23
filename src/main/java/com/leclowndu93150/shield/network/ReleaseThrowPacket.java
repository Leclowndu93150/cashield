package com.leclowndu93150.shield.network;

import com.leclowndu93150.shield.Shield;
import com.leclowndu93150.shield.item.CaptainAmericaShieldItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReleaseThrowPacket {
    
    public ReleaseThrowPacket() {
    }

    public static void encode(ReleaseThrowPacket packet, FriendlyByteBuf buf) {
        // No data needed
    }

    public static ReleaseThrowPacket decode(FriendlyByteBuf buf) {
        return new ReleaseThrowPacket();
    }

    public static void handle(ReleaseThrowPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.getPersistentData().getBoolean("shield:isCharging")) {
                int startTime = player.getPersistentData().getInt("shield:throwStartTime");
                int chargeTime = player.tickCount - startTime;
                
                // Check if player is holding the shield
                ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
                
                if (mainHand.getItem() instanceof CaptainAmericaShieldItem shieldItem) {
                    shieldItem.throwShield(player, player.level, mainHand, chargeTime);
                } else if (offHand.getItem() instanceof CaptainAmericaShieldItem shieldItem) {
                    shieldItem.throwShield(player, player.level, offHand, chargeTime);
                }
                
                player.getPersistentData().putBoolean("shield:isCharging", false);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}