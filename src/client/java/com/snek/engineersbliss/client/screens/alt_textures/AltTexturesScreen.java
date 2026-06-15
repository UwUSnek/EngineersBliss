package com.snek.engineersbliss.client.screens.alt_textures;

import java.util.Map;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.screens.__base_PauseScreen;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class AltTexturesScreen extends __base_PauseScreen {


    public AltTexturesScreen() {
        super();
    }

    @Override
    protected void init() {
        addButton(getToggleText(Blocks.SLIME_BLOCK,    AltTexturesHandler.getFeature(Blocks.SLIME_BLOCK)),    b -> toggleFeature(Blocks.SLIME_BLOCK,    b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, 200);
        addButton(getToggleText(Blocks.HONEY_BLOCK,    AltTexturesHandler.getFeature(Blocks.HONEY_BLOCK)),    b -> toggleFeature(Blocks.HONEY_BLOCK,    b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, 200);
        addButton(getToggleText(Blocks.MANGROVE_ROOTS, AltTexturesHandler.getFeature(Blocks.MANGROVE_ROOTS)), b -> toggleFeature(Blocks.MANGROVE_ROOTS, b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, 200);
        addButton(getToggleText(Blocks.SCAFFOLDING,    AltTexturesHandler.getFeature(Blocks.SCAFFOLDING)),    b -> toggleFeature(Blocks.SCAFFOLDING,    b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 3, 200);
        addButton(getToggleText(Blocks.REDSTONE_WIRE,  AltTexturesHandler.getFeature(Blocks.REDSTONE_WIRE)),  b -> toggleFeature(Blocks.REDSTONE_WIRE,  b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 4, 200);
    }


    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        //TODO stuff
    }





    private static final Map<Block, String> BUTTON_NAMES = Map.of(
        Blocks.SLIME_BLOCK,    "Transparent Slime Block",
        Blocks.HONEY_BLOCK,    "Transparent Honey Block",
        Blocks.MANGROVE_ROOTS, "Unobstructive Mangrove Roots",
        Blocks.SCAFFOLDING,    "Unobstructive Scaffolding",
        Blocks.REDSTONE_WIRE,  "Line Redstone Dust"
    );
    public String getToggleText(final Block block, final boolean state) {
        return BUTTON_NAMES.get(block) + ": " + (state ? "ON" : "OFF");
    }


    public void toggleFeature(final Block block, final Button b) {
        boolean newState = !AltTexturesHandler.getFeature(block);
        b.setMessage(Component.literal(getToggleText(block, newState)));
        AltTexturesHandler.setFeature(block, newState);
        MinecraftUtils.refreshSectionsContaining(block);
    }
}