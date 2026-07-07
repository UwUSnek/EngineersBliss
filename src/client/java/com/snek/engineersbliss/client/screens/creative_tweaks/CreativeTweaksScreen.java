package com.snek.engineersbliss.client.screens.creative_tweaks;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.parts.UiSteppedSlider;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;




public class CreativeTweaksScreen extends __base_Screen {
    private static final int BUTTON_WIDTH = 200;



    public CreativeTweaksScreen() {
        super();
    }




    @Override
    protected void init() {



        //TODO name header: Player properties
        addRenderableWidget(new UiSteppedSlider<Float>(
            BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH, BUTTON_HEIGHT,
            "Flying speed", List.of(0.05f, 0.125f, 0.25f, 0.5f, 1f, 2f, 4f, 8f, 16f, 32f, 64f), 0, CreativeTweaksHandler::onFlyingSpeedChange
        ));
        addRenderableWidget(new UiSteppedSlider<Float>(
            BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH, BUTTON_HEIGHT,
            "Reach distance", List.of(4.5f, 8f, 16f, 32f, 64f, 128f, 256f, 8192f), 0, CreativeTweaksHandler::onReachDistanceChange
        ));
        addRenderableWidget(new UiSteppedSlider<Integer>(
            BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH, BUTTON_HEIGHT,
            "Interaction radius", List.of(1, 2, 3, 4, 5, 10, 20), 0, CreativeTweaksHandler::onInteractionRadiusChanged
        ));
        addButton(getToggleText(CreativeTweakFeature.NO_SIGN_GUI),                     CreativeTweakFeature.NO_SIGN_GUI                    .getDetails(), b -> toggleFeature(CreativeTweakFeature.NO_SIGN_GUI,                     b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 0, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  3, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.OPEN_OBSTRUCTED_CONTAINERS),      CreativeTweakFeature.OPEN_OBSTRUCTED_CONTAINERS     .getDetails(), b -> toggleFeature(CreativeTweakFeature.OPEN_OBSTRUCTED_CONTAINERS,      b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 0, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  4, BUTTON_WIDTH);



        //TODO name header: World interactions
        addButton(getToggleText(CreativeTweakFeature.PHASE_THROUGH_BLOCKS_FLY),        CreativeTweakFeature.PHASE_THROUGH_BLOCKS_FLY       .getDetails(), b -> toggleFeature(CreativeTweakFeature.PHASE_THROUGH_BLOCKS_FLY,        b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  0, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.PHASE_THROUGH_ENTITIES),          CreativeTweakFeature.PHASE_THROUGH_ENTITIES         .getDetails(), b -> toggleFeature(CreativeTweakFeature.PHASE_THROUGH_ENTITIES,          b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  1, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_FIRE_EFFECT),             CreativeTweakFeature.DISABLE_FIRE_EFFECT            .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_FIRE_EFFECT,             b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  2, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_FREEZING_EFFECT),         CreativeTweakFeature.DISABLE_FREEZING_EFFECT        .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_FREEZING_EFFECT,         b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  3, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_HONEY_JUMP),              CreativeTweakFeature.DISABLE_HONEY_JUMP             .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_HONEY_JUMP,              b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  4, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_HONEY_SLIDING),           CreativeTweakFeature.DISABLE_HONEY_SLIDING          .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_HONEY_SLIDING,           b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  5, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_SLIME_BOUNCE),            CreativeTweakFeature.DISABLE_SLIME_BOUNCE           .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_SLIME_BOUNCE,            b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  6, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_BED_BOUNCE),              CreativeTweakFeature.DISABLE_BED_BOUNCE             .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_BED_BOUNCE,              b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  7, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_ICE_SLIDING),             CreativeTweakFeature.DISABLE_ICE_SLIDING            .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_ICE_SLIDING,             b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  8, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_CURRENT_DRAG),            CreativeTweakFeature.DISABLE_CURRENT_DRAG           .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_CURRENT_DRAG,            b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  9, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_BUBBLE_COLUMN_DRAG),      CreativeTweakFeature.DISABLE_BUBBLE_COLUMN_DRAG     .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_BUBBLE_COLUMN_DRAG,      b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 10, BUTTON_WIDTH);

        //TODO name header: Speed debuff suppressors
        addButton(getToggleText(CreativeTweakFeature.DISABLE_HONEY_SLOWDOWN),          CreativeTweakFeature.DISABLE_HONEY_SLOWDOWN         .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_HONEY_SLOWDOWN,          b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  0, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN),          CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN         .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN,          b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  1, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_SOULSAND_SLOWDOWN),       CreativeTweakFeature.DISABLE_SOULSAND_SLOWDOWN      .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_SOULSAND_SLOWDOWN,       b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  2, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_POWDER_SNOW_SLOWDOWN),    CreativeTweakFeature.DISABLE_POWDER_SNOW_SLOWDOWN   .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_POWDER_SNOW_SLOWDOWN,    b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  3, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_WATER_SLOWDOWN),          CreativeTweakFeature.DISABLE_WATER_SLOWDOWN         .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_WATER_SLOWDOWN,          b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  4, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_LAVA_SLOWDOWN),           CreativeTweakFeature.DISABLE_LAVA_SLOWDOWN          .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_LAVA_SLOWDOWN,           b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  5, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_COBWEB_SLOWDOWN),         CreativeTweakFeature.DISABLE_COBWEB_SLOWDOWN        .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_COBWEB_SLOWDOWN,         b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  6, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_LADDER_SLOWDOWN),         CreativeTweakFeature.DISABLE_LADDER_SLOWDOWN        .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_LADDER_SLOWDOWN,         b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  7, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_VINES_SLOWDOWN),          CreativeTweakFeature.DISABLE_VINES_SLOWDOWN         .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_VINES_SLOWDOWN,          b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  8, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_TWISTING_VINES_SLOWDOWN), CreativeTweakFeature.DISABLE_TWISTING_VINES_SLOWDOWN.getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_TWISTING_VINES_SLOWDOWN, b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) *  9, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_WEEPING_VINES_SLOWDOWN),  CreativeTweakFeature.DISABLE_WEEPING_VINES_SLOWDOWN .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_WEEPING_VINES_SLOWDOWN,  b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 10, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_SWEET_BERRIES_SLOWDOWN),  CreativeTweakFeature.DISABLE_SWEET_BERRIES_SLOWDOWN .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_SWEET_BERRIES_SLOWDOWN,  b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 11, BUTTON_WIDTH);

        //TODO name header: Visual clutter
        addButton(getToggleText(CreativeTweakFeature.DISABLE_ITEM_CHANGE_ANIMATION),   CreativeTweakFeature.DISABLE_ITEM_CHANGE_ANIMATION  .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_ITEM_CHANGE_ANIMATION,   b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 3, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 0, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_HAND_SWING_ANIMATION),    CreativeTweakFeature.DISABLE_HAND_SWING_ANIMATION   .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_HAND_SWING_ANIMATION,    b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 3, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 1, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_DIMENSION_CHANGE_SCREEN), CreativeTweakFeature.DISABLE_DIMENSION_CHANGE_SCREEN.getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_DIMENSION_CHANGE_SCREEN, b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 3, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 2, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_WATER_FOV_CHANGE),        CreativeTweakFeature.DISABLE_WATER_FOV_CHANGE       .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_WATER_FOV_CHANGE,        b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 3, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 3, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_WATER_OVERLAY),           CreativeTweakFeature.DISABLE_WATER_OVERLAY          .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_WATER_OVERLAY,           b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 3, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 4, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_LAVA_OVERLAY),            CreativeTweakFeature.DISABLE_LAVA_OVERLAY           .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_LAVA_OVERLAY,            b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 3, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 5, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_NETHER_PORTAL_OVERLAY),   CreativeTweakFeature.DISABLE_NETHER_PORTAL_OVERLAY  .getDetails(), b -> toggleFeature(CreativeTweakFeature.DISABLE_NETHER_PORTAL_OVERLAY,   b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 3, LIST_TOP + (BORDER_HEIGHT + BUTTON_HEIGHT) * 6, BUTTON_WIDTH);
    }




    public static Txt getToggleText(final CreativeTweakFeature feature, final boolean state) {
        return feature.getName().cat(": " + (state ? "ON" : "OFF"));
    }
    public static Txt getToggleText(final CreativeTweakFeature feature) {
        return getToggleText(feature, CreativeTweaksHandler.clientPlayerHasFeature(Minecraft.getInstance().player, feature));
    }


    public static void toggleFeature(final CreativeTweakFeature feature, final Button b) {
        final boolean newState = !CreativeTweaksHandler.clientPlayerHasFeature(Minecraft.getInstance().player, feature);
        b.setMessage(getToggleText(feature, newState).get());
        CreativeTweaksHandler.setFeature(feature, newState);
    }
}