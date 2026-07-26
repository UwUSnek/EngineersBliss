package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import com.snek.engineersbliss.EngineerSBliss;

import java.util.function.Supplier;

import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;








@SuppressWarnings("java:S1905")
public class CreativeTweaksClientFeatureSet extends __base_ClientFeatureSet<CreativeTweaksServerFeatureSet> {
    public static final CreativeTweaksClientFeatureSet INSTANCE = new CreativeTweaksClientFeatureSet();
    private CreativeTweaksClientFeatureSet() {
        super(CreativeTweaksServerFeatureSet.INSTANCE, () -> new UiTxt("Creative Tweaks"));
    }



    public static final ClientFeature<?> WALKING_SPEED = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.WALKING_SPEED,
        () -> new UiTxt("Walking speed"),
        () -> new UiTxt()
            .cat(new UiTxt("Controls your walking speed.\n"))
            .cat(new UiTxt("Values represent multiples of the default walking speed."))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> FLYING_SPEED = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.FLYING_SPEED,
        () -> new UiTxt("Flying speed"),
        () -> new UiTxt()
            .cat(new UiTxt("Controls your flying speed.\n"))
            .cat(new UiTxt("Values represent multiples of the default Creative Mode flying speed."))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> INTERACTION_DISTANCE = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.INTERACTION_DISTANCE,
        () -> new UiTxt("Interaction distance"),
        () -> new UiTxt()
            .cat(new UiTxt("Controls your reach distance.\n"))
            .cat(new UiTxt("Values represent the maximum distance you can interact with blocks and entities at, measured in Blocks."))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> INTERACTION_RADIUS = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.INTERACTION_RADIUS,
        () -> new UiTxt("Interaction radius"),
        () -> new UiTxt()
            .cat(new UiTxt("Controls your interaction radius. This lets you place, break or use multiple blocks at once.\n"))
            .cat(new UiTxt("Values represent the maximum distance from your targeted position in which blocks respond to left and right clicks."))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> INTERACTION_COUNT = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.INTERACTION_COUNT,
        () -> new UiTxt("Interaction count"),
        () -> new UiTxt()
            .cat(new UiTxt("Controls your interaction count. This lets you place, break, or use multiple blocks at once.\n"))
            .cat(new UiTxt("Values represent the amount of times your interactions are processed. 1 is Vanilla's default."))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> NO_SIGN_GUI = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.NO_SIGN_GUI,
        () -> new UiTxt("Suppress Sign GUI"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops the Edit Sign GUI from showing up after placing a Sign or Hanging Sign.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> OPEN_OBSTRUCTED_CONTAINERS = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.OPEN_OBSTRUCTED_CONTAINERS,
        () -> new UiTxt("Open obstructed containers"),
        () -> new UiTxt()
            .cat(new UiTxt("Lets you open Chests, Trapped Chests, Ender Chests and Copper Chests even when they are below a solid block.\n"))
            .cat(new UiTxt("This also includes double chest variants.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get()).cat(new UiTxt("\n"))
            .cat(Notices.MULTIPLAYER_NOTICE.get()) //TODO actually check if this is not possible in multiplayer
    );




    public static final ClientFeature<?> PHASE_THROUGH_BLOCKS_FLY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.PHASE_THROUGH_BLOCKS_FLY,
        () -> new UiTxt("Phase through blocks"),
        () -> new UiTxt()
            .cat(new UiTxt("Lets you phase through blocks while flying, completely ignoring their collisions.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get()).cat(new UiTxt("\n"))
            .cat(Notices.MULTIPLAYER_NOTICE.get())
    );
    public static final ClientFeature<?> PHASE_THROUGH_ENTITIES = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.PHASE_THROUGH_ENTITIES,
        () -> new UiTxt("Phase through entities"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from pushing and being pushed by other entities, even while not flying.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get()).cat(new UiTxt("\n"))
            .cat(Notices.MULTIPLAYER_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_FIRE_EFFECT = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_FIRE_EFFECT,
        () -> new UiTxt("Disable being on fire"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from being on fire while standing in Fire, Soul Fire, Lava, Lava Cauldrons, Campfires and Soul Campfires.\n"))
            .cat(new UiTxt("This also disables the Fire overlay that is normally shown when the player is on fire.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_FREEZING_EFFECT = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_FREEZING_EFFECT,
        () -> new UiTxt("Disable freezing"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from freezing while inside Powder Snow.\n"))
            .cat(new UiTxt("This also disables the Freezing overlay and the FOV decrease that are normally applied when the player is freezing.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> FIX_HONEY_JUMP = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.FIX_HONEY_JUMP,
        () -> new UiTxt("Fix Honey Block jump"),
        () -> new UiTxt()
            .cat(new UiTxt("Lets you jump while standing on Honey Blocks.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_HONEY_SLIDING = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_HONEY_SLIDING,
        () -> new UiTxt("Disable Honey Block sliding"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from sticking to the sides of Honey Blocks while falling or jumping, effectively removing their custom sliding physics.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_SLIME_BOUNCE = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_SLIME_BOUNCE,
        () -> new UiTxt("Disable bouncing on Slime Blocks"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from bouncing on Slime Blocks.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_BED_BOUNCE = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_BED_BOUNCE,
        () -> new UiTxt("Disable bouncing on beds"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from bouncing on Beds.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_ICE_SLIDING = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_ICE_SLIDING,
        () -> new UiTxt("Disable sliding on ice"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from sliding on Frosted Ice, Ice, Packed Ice, and Blue Ice.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_CURRENT_DRAG = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_CURRENT_DRAG,
        () -> new UiTxt("Disable fluid current drag"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from being moved by Water and Lava currents.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_BUBBLE_COLUMN_DRAG = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_BUBBLE_COLUMN_DRAG,
        () -> new UiTxt("Disable Bubble Column drag"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from being pushed up or pulled down while standing in Bubble Columns.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );




    public static final ClientFeature<?> DISABLE_HONEY_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_HONEY_SLOWDOWN,
        () -> new UiTxt("Disable Honey Block slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking on Honey Blocks.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_SLIME_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_SLIME_SLOWDOWN,
        () -> new UiTxt("Disable Slime Block slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking on Slime Blocks.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_SOULSAND_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_SOULSAND_SLOWDOWN,
        () -> new UiTxt("Disable Soul Sand slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking on Soul Sand.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_POWDER_SNOW_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_POWDER_SNOW_SLOWDOWN,
        () -> new UiTxt("Disable Powder Snow slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Powder Snow.\n"))
            .cat(new UiTxt("This also lets you jump freely while inside Powder Snow.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_WATER_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_WATER_SLOWDOWN,
        () -> new UiTxt("Disable Water slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Water.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_LAVA_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_LAVA_SLOWDOWN,
        () -> new UiTxt("Disable Lava slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Lava.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_COBWEB_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_COBWEB_SLOWDOWN,
        () -> new UiTxt("Disable Cobweb slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Cobwebs.\n"))
            .cat(new UiTxt("This also lets you jump freely while inside Cobwebs.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_LADDER_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_LADDER_SLOWDOWN,
        () -> new UiTxt("Disable Ladder slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking near Ladders.\n"))
            .cat(new UiTxt("This doesn't let you jump while inside Ladders: Being a Climbable block, the jump button initiates the Climb action.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_VINES_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_VINES_SLOWDOWN,
        () -> new UiTxt("Disable Vines slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking near Vines.\n"))
            .cat(new UiTxt("This doesn't let you jump while inside Vines: Being a Climbable block, the jump button initiates the Climb action.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_TWISTING_VINES_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_TWISTING_VINES_SLOWDOWN,
        () -> new UiTxt("Disable Twisting Vines slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Twisting Vines.\n"))
            .cat(new UiTxt("This doesn't let you jump while inside Twisting Vines: Being a Climbable block, the jump button initiates the Climb action.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_WEEPING_VINES_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_WEEPING_VINES_SLOWDOWN,
        () -> new UiTxt("Disable Weeping Vines slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Weeping Vines.\n"))
            .cat(new UiTxt("This doesn't let you jump while inside Weeping Vines: Being a Climbable block, the jump button initiates the Climb action.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_SWEET_BERRIES_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_SWEET_BERRIES_SLOWDOWN,
        () -> new UiTxt("Disable Sweet Berry Bush slowdown"),
        () -> new UiTxt()
            .cat(new UiTxt("Prevents you from being slowed down while walking through Sweet Berry Bushes.\n"))
            .cat(new UiTxt("This also lets you jump freely while inside Sweet Berry Bushes.\n"))
            .cat(Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE.get())
    );




    public static final ClientFeature<?> DISABLE_ITEM_CHANGE_ANIMATION = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_ITEM_CHANGE_ANIMATION,
        () -> new UiTxt("Disable item change animation"),
        () -> new UiTxt()
            .cat(new UiTxt("Disables the animation that plays when you select a different item or the item you are holding changes.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_HAND_SWING_ANIMATION = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_HAND_SWING_ANIMATION,
        () -> new UiTxt("Disable hand swing animation"),
        () -> new UiTxt()
            .cat(new UiTxt("Disables the animation that plays when you interact with or attack blocks or entities, or use certain items.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_DIMENSION_CHANGE_SCREEN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_DIMENSION_CHANGE_SCREEN,
        () -> new UiTxt("Disable dimension change screen"),
        () -> new UiTxt()
            .cat(new UiTxt("Disables the loading screen that appears when changing dimensions.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_WATER_FOV_CHANGE = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_WATER_FOV_CHANGE,
        () -> new UiTxt("Disable Water FOV"),
        () -> new UiTxt()
            .cat(new UiTxt("Stops you from having reduced FOV while inside Water.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_WATER_OVERLAY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_WATER_OVERLAY,
        () -> new UiTxt("Disable overlay effect of Water"),
        () -> new UiTxt()
            .cat(new UiTxt("Disables the overlay and tint effects that appear while inside Water.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_LAVA_OVERLAY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_LAVA_OVERLAY,
        () -> new UiTxt("Disable overlay effect of Lava"),
        () -> new UiTxt()
            .cat(new UiTxt("Disables the overlay and tint effects that appear while inside Lava.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );
    public static final ClientFeature<?> DISABLE_NETHER_PORTAL_OVERLAY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_NETHER_PORTAL_OVERLAY,
        () -> new UiTxt("Disable Nether Portal overlay"),
        () -> new UiTxt()
            .cat(new UiTxt("Disables the overlay effect that appears while inside Nether Portal blocks.\n"))
            .cat(Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE.get())
    );




    private class Notices {
        public static final Supplier<UiTxt> CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE = () -> (UiTxt)new UiTxt(
            "This only works while in Creative Mode and doesn't affect other entities."
        ).yellow();

        public static final Supplier<UiTxt> CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE = () -> (UiTxt)new UiTxt(
            "This only works while in Creative Mode and doesn't affect other players."
        ).yellow();

        public static final Supplier<UiTxt> MULTIPLAYER_NOTICE = () -> (UiTxt)new UiTxt(
            "This doesn't work on servers without the " + EngineerSBliss.MOD_NAME + " mod installed."
        ).red();
    }
}
