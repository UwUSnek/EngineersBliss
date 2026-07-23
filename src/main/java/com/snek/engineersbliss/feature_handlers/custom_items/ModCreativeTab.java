package com.snek.engineersbliss.feature_handlers.custom_items;

import com.snek.engineersbliss.EngineerSBliss;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;




public class ModCreativeTab {

    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(
        BuiltInRegistries.CREATIVE_MODE_TAB.key(),
        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "engineers-bliss")
    );

    public static final CreativeModeTab TAB = FabricCreativeModeTab.builder()
        .icon(() -> new ItemStack(CustomItemHandler.GREEN_SCREEN.asItem()))
        .title(Component.translatable("creativeTab." + EngineerSBliss.MOD_ID + ".engineers-bliss"))
        .displayItems((params, output) -> {
            output.accept(CustomItemHandler.GREEN_SCREEN);
            output.accept(CustomItemHandler.BLUE_SCREEN);
        })
        .build()
    ;

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, TAB);
    }
}