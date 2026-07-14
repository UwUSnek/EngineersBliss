package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import com.snek.engineersbliss.utils.Txt;
import com.snek.engineersbliss.EngineerSBliss;

import java.util.function.Supplier;

import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakServerFeature;








public enum CreativeTweakFeature {

    //! Sliders are not toggle features

    NO_SIGN_GUI(CreativeTweakServerFeature.NO_SIGN_GUI,
        () -> new UiTxt("Suppress Sign GUI"),
        () -> new Txt()
            .cat(new UiTxt("Stops the Edit Sign GUI from showing up after placing a Sign or Hanging Sign.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    ),
    OPEN_OBSTRUCTED_CONTAINERS(CreativeTweakServerFeature.OPEN_OBSTRUCTED_CONTAINERS,
        () -> new UiTxt("Open obstructed containers"),
        () -> new Txt()
            .cat(new UiTxt("Lets you open Chests, Trapped Chests, Ender Chests and Copper Chests even when they are below a solid block.\n"))
            .cat(new UiTxt("This also includes double chest variants.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get()).cat("\n")
            .cat(Notices.MULTIPLAYER_NOTICE.get()) //TODO actually check if this is not possible in multiplayer
    ),




    PHASE_THROUGH_BLOCKS_FLY(CreativeTweakServerFeature.PHASE_THROUGH_BLOCKS_FLY,
        () -> new UiTxt("Phase through blocks"),
        () -> new Txt()
            .cat(new UiTxt("Lets you phase through blocks while flying, completely ignoring their collisions.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get()).cat("\n")
            .cat(Notices.MULTIPLAYER_NOTICE.get())
    ),
    PHASE_THROUGH_ENTITIES(CreativeTweakServerFeature.PHASE_THROUGH_ENTITIES,
        () -> new UiTxt("Phase through entities"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from pushing and being pushed by other entities, even while not flying.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get()).cat("\n")
            .cat(Notices.MULTIPLAYER_NOTICE.get())
    ),
    DISABLE_FIRE_EFFECT(CreativeTweakServerFeature.DISABLE_FIRE_EFFECT,
        () -> new UiTxt("Disable being on fire"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from being on fire while standing in Fire, Soul Fire, Lava, Lava Cauldrons, Campfires and Soul Campfires.\n"))
            .cat(new UiTxt("This also disables the Fire overlay that is normally shown when the player is on fire.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_FREEZING_EFFECT(CreativeTweakServerFeature.DISABLE_FREEZING_EFFECT,
        () -> new UiTxt("Disable freezing"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from freezing while inside Powder Snow.\n"))
            .cat(new UiTxt("This also disables the Freezing overlay and the FOV decrease that are normally applied when the player is freezing.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    FIX_HONEY_JUMP(CreativeTweakServerFeature.FIX_HONEY_JUMP,
        () -> new UiTxt("Fix Honey Block jump"),
        () -> new Txt()
            .cat(new UiTxt("Lets you jump while standing on Honey Blocks.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ), //TODO
    DISABLE_HONEY_SLIDING(CreativeTweakServerFeature.DISABLE_HONEY_SLIDING,
        () -> new UiTxt("Disable Honey Block sliding"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from sticking to the sides of Honey Blocks while falling or jumping, effectively removing their custom sliding physics.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ), //TODO
    DISABLE_SLIME_BOUNCE(CreativeTweakServerFeature.DISABLE_SLIME_BOUNCE,
        () -> new UiTxt("Disable bouncing on Slime Blocks"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from bouncing on Slime Blocks.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_BED_BOUNCE(CreativeTweakServerFeature.DISABLE_BED_BOUNCE,
        () -> new UiTxt("Disable bouncing on beds"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from bouncing on Beds.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_ICE_SLIDING(CreativeTweakServerFeature.DISABLE_ICE_SLIDING,
        () -> new UiTxt("Disable sliding on ice"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from sliding on Frosted Ice, Ice, Packed Ice, and Blue Ice.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_CURRENT_DRAG(CreativeTweakServerFeature.DISABLE_CURRENT_DRAG,
        () -> new UiTxt("Disable fluid current drag"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from being moved by Water and Lava currents.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_BUBBLE_COLUMN_DRAG(CreativeTweakServerFeature.DISABLE_BUBBLE_COLUMN_DRAG,
        () -> new UiTxt("Disable Bubble Column drag"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from being pushed up or pulled down while standing in Bubble Columns.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ), //TODO




    DISABLE_HONEY_SLOWDOWN(CreativeTweakServerFeature.DISABLE_HONEY_SLOWDOWN,
        () -> new UiTxt("Disable Honey Blocks slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking on Honey Blocks.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_SLIME_SLOWDOWN(CreativeTweakServerFeature.DISABLE_SLIME_SLOWDOWN,
        () -> new UiTxt("Disable Slime Blocks slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking on Slime Blocks.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_SOULSAND_SLOWDOWN(CreativeTweakServerFeature.DISABLE_SOULSAND_SLOWDOWN,
        () -> new UiTxt("Disable Soul Sand slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking on Soul Sand.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_POWDER_SNOW_SLOWDOWN(CreativeTweakServerFeature.DISABLE_POWDER_SNOW_SLOWDOWN,
        () -> new UiTxt("Disable Powder Snow slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Powder Snow.\n"))
            .cat(new UiTxt("This also lets you jump freely while inside Powder Snow.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_WATER_SLOWDOWN(CreativeTweakServerFeature.DISABLE_WATER_SLOWDOWN,
        () -> new UiTxt("Disable Water slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Water.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_LAVA_SLOWDOWN(CreativeTweakServerFeature.DISABLE_LAVA_SLOWDOWN,
        () -> new UiTxt("Disable Lava slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Lava.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_COBWEB_SLOWDOWN(CreativeTweakServerFeature.DISABLE_COBWEB_SLOWDOWN,
        () -> new UiTxt("Disable Cobwebs slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Cobwebs.\n"))
            .cat(new UiTxt("This also lets you jump freely while inside Cobwebs.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_LADDER_SLOWDOWN(CreativeTweakServerFeature.DISABLE_LADDER_SLOWDOWN,
        () -> new UiTxt("Disable Ladders slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking near Ladders.\n"))
            .cat(new UiTxt("This doesn't let you jump while inside Ladders: Being a Climbable block, the jump button initiates the Climb action.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_VINES_SLOWDOWN(CreativeTweakServerFeature.DISABLE_VINES_SLOWDOWN,
        () -> new UiTxt("Disable Vines slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking near Vines.\n"))
            .cat(new UiTxt("This doesn't let you jump while inside Vines: Being a Climbable block, the jump button initiates the Climb action.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_TWISTING_VINES_SLOWDOWN(CreativeTweakServerFeature.DISABLE_TWISTING_VINES_SLOWDOWN,
        () -> new UiTxt("Disable Twisting Vines slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Twisting Vines.\n"))
            .cat(new UiTxt("This doesn't let you jump while inside Twisting Vines: Being a Climbable block, the jump button initiates the Climb action.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_WEEPING_VINES_SLOWDOWN(CreativeTweakServerFeature.DISABLE_WEEPING_VINES_SLOWDOWN,
        () -> new UiTxt("Disable Weeping Vines slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Weeping Vines.\n"))
            .cat(new UiTxt("This doesn't let you jump while inside Weeping Vines: Being a Climbable block, the jump button initiates the Climb action.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_SWEET_BERRIES_SLOWDOWN(CreativeTweakServerFeature.DISABLE_SWEET_BERRIES_SLOWDOWN,
        () -> new UiTxt("Disable Sweet Berry Bush slowdown"),
        () -> new Txt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Sweet Berry Bushes.\n"))
            .cat(new UiTxt("This also lets you jump freely while inside Sweet Berry Bushes.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    ),




    DISABLE_ITEM_CHANGE_ANIMATION(CreativeTweakServerFeature.DISABLE_ITEM_CHANGE_ANIMATION,
        () -> new UiTxt("Disable item change animation"),
        () -> new Txt()
            .cat(new UiTxt("Disables the animation that plays when you select a different item or the item you are holding changes.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    ), //TODO
    DISABLE_HAND_SWING_ANIMATION(CreativeTweakServerFeature.DISABLE_HAND_SWING_ANIMATION,
        () -> new UiTxt("Disable hand swing animation"),
        () -> new Txt()
            .cat(new UiTxt("Disables the animation that plays when you interact with or attack blocks or entities, or use certain items.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    ), //TODO
    DISABLE_DIMENSION_CHANGE_SCREEN(CreativeTweakServerFeature.DISABLE_DIMENSION_CHANGE_SCREEN,
        () -> new UiTxt("Disable dimension change screen"),
        () -> new Txt()
            .cat(new UiTxt("Disables the loading screen that appears when changing dimensions.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    ), //TODO
    DISABLE_WATER_FOV_CHANGE(CreativeTweakServerFeature.DISABLE_WATER_FOV_CHANGE,
        () -> new UiTxt("Disable Water FOV"),
        () -> new Txt()
            .cat(new UiTxt("Stops you from having reduced FOV while inside Water.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    ), //TODO
    DISABLE_WATER_OVERLAY(CreativeTweakServerFeature.DISABLE_WATER_OVERLAY,
        () -> new UiTxt("Disable overlay effect of Water"),
        () -> new Txt()
            .cat(new UiTxt("Disables the overlay and tint effects that appear while inside Water.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    ),
    DISABLE_LAVA_OVERLAY(CreativeTweakServerFeature.DISABLE_LAVA_OVERLAY,
        () -> new UiTxt("Disable overlay effect of Lava"),
        () -> new Txt()
            .cat(new UiTxt("Disables the overlay and tint effects that appear while inside Lava.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    ), //TODO
    DISABLE_NETHER_PORTAL_OVERLAY(CreativeTweakServerFeature.DISABLE_NETHER_PORTAL_OVERLAY,
        () -> new UiTxt("Disable Nether Portal overlay"),
        () -> new Txt()
            .cat(new UiTxt("Disables the overlay effect that appears while inside Nether Portal blocks.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    ); //TODO




    private class Notices {
        public static final Supplier<Txt> CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE = () -> new UiTxt(
            "This only works while in Creative Mode and doesn't affect other entities."
        ).yellow();

        public static final Supplier<Txt> CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE = () -> new UiTxt(
            "This only works while in Creative Mode and doesn't affect other players."
        ).yellow();

        public static final Supplier<Txt> MULTIPLAYER_NOTICE = () -> new UiTxt(
            "This doesn't work on servers without the " + EngineerSBliss.MOD_NAME + " mod installed."
        ).red();
    }








    // Name and properties
    //! Txt values are computed lazily as they depend on the Minecraft window and cannot be calculated during static initialization
    private final CreativeTweakServerFeature serverFeature;
    private final Supplier<Txt> nameSupplier;
    private final Supplier<Txt> detailsSupplier;
    private final long flagBit; //! Flag bit index is calculated from the order of declaration
    //! Defaults defined in CreativeTweakServerFeature


    // Getters and checks //! Name and details must be recomputed as they depend on the GUI scale option
    public CreativeTweakServerFeature getServerFeature() { return serverFeature; }
    public Txt getName   () { return nameSupplier.get(); }
    public Txt getDetails() { return detailsSupplier.get(); }
    public long getFlagBit() { return flagBit; }
    public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }
    //! Defaults defined in CreativeTweakServerFeature


    private CreativeTweakFeature(final CreativeTweakServerFeature serverFeature, final Supplier<Txt> nameSupplier, final Supplier<Txt> detailsSupplier) {
        this.serverFeature = serverFeature;
        this.nameSupplier    = nameSupplier;
        this.detailsSupplier = detailsSupplier;
        this.flagBit = serverFeature.getFlagBit();
    }
}
