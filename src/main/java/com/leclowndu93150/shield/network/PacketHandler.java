package com.leclowndu93150.shield.network;

import com.leclowndu93150.shield.Shield;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Shield.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, StartThrowPacket.class, StartThrowPacket::encode, StartThrowPacket::decode, StartThrowPacket::handle);
        INSTANCE.registerMessage(id++, ReleaseThrowPacket.class, ReleaseThrowPacket::encode, ReleaseThrowPacket::decode, ReleaseThrowPacket::handle);
    }
}