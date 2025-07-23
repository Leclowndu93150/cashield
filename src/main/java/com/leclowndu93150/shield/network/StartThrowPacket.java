package com.leclowndu93150.shield.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StartThrowPacket {
    
    public StartThrowPacket() {
    }

    public static void encode(StartThrowPacket packet, FriendlyByteBuf buf) {
        // No data needed
    }

    public static StartThrowPacket decode(FriendlyByteBuf buf) {
        return new StartThrowPacket();
    }

    public static void handle(StartThrowPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.getPersistentData().putInt("shield:throwStartTime", player.tickCount);
                player.getPersistentData().putBoolean("shield:isCharging", true);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}