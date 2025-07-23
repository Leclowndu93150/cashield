package com.leclowndu93150.shield.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class KeyBindings {
    public static final String KEY_CATEGORY_SHIELD = "key.category.shield";
    public static final String KEY_THROW_SHIELD = "key.shield.throw";

    public static KeyMapping throwShieldKey;

    public static void register(RegisterKeyMappingsEvent event) {
        throwShieldKey = new KeyMapping(
                KEY_THROW_SHIELD,
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KEY_CATEGORY_SHIELD
        );
        event.register(throwShieldKey);
    }
}