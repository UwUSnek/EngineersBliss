package com.snek.engineersbliss.client.screens.rendering;

import java.util.function.Consumer;

import com.snek.engineersbliss.client.feature_handlers.RenderFilterHandler;
import com.snek.engineersbliss.client.screens.Layout;
import com.snek.engineersbliss.client.screens.__base_PauseScreen;
import com.snek.engineersbliss.client.screens.rendering.widgets.BlockListWidget;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;




public class AltTexturesScreen extends __base_PauseScreen {


    public AltTexturesScreen() {
        super();
    }

    @Override
    protected void init() {

    }


    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {

    }




    // public String getToggleText_unobstructiveSlimeBlock(final boolean state) {
    //     return "Render fluids: " + (state ? "YES" : "NO");
    // }
    // public void toggleUnobstructiveSlimeBlock(final Button b) {
    //     boolean newState = !changedRenderFluids;
    //     changedRenderFluids = newState;
    //     markChanged(); //! Flushed on application
    //     b.setMessage(Component.literal(getToggleText_renderFluids(newState)));
    // }
}