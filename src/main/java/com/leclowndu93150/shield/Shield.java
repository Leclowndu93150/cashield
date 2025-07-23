package com.leclowndu93150.shield;

import com.leclowndu93150.shield.entity.ThrownShield;
import com.leclowndu93150.shield.item.CaptainAmericaShieldItem;
import com.leclowndu93150.shield.renderer.ThrownShieldRenderer;
import com.leclowndu93150.shield.network.PacketHandler;
import com.leclowndu93150.shield.client.KeyBindings;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.slf4j.Logger;

@Mod(Shield.MODID)
public class Shield {

    public static final String MODID = "shield";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<Item> CAPTAIN_AMERICA_SHIELD = ITEMS.register("captain_america_shield",
            () -> new CaptainAmericaShieldItem(new Item.Properties()
                    .tab(CreativeModeTab.TAB_COMBAT)
                    .durability(336)));

    public static final RegistryObject<EntityType<ThrownShield>> THROWN_SHIELD = ENTITY_TYPES.register("thrown_shield",
            () -> EntityType.Builder.<ThrownShield>of(ThrownShield::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("thrown_shield"));

    public Shield() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::registerKeys);

        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
        LOGGER.info("Captain America Shield mod initialized!");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        EntityRenderers.register(THROWN_SHIELD.get(), ThrownShieldRenderer::new);

        ItemProperties.register(CAPTAIN_AMERICA_SHIELD.get(),
                new ResourceLocation("blocking"),
                (stack, level, entity, seed) ->
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );
    }
    
    private void registerKeys(RegisterKeyMappingsEvent event) {
        KeyBindings.register(event);
    }
}