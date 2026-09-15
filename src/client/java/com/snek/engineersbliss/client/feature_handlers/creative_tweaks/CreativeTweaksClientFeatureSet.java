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
        s(() -> new UiTxt("Walking speed")),
        s(() -> new UiTxt("Controls your walking speed.")),
        s(() -> new UiTxt("Values represent multiples of the default walking speed.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> FLYING_SPEED = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.FLYING_SPEED,
        s(() -> new UiTxt("Flying speed")),
        s(() -> new UiTxt("Controls your flying speed.")),
        s(() -> new UiTxt("Values represent multiples of the default Creative Mode flying speed.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> PLAYER_SCALE = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.PLAYER_SCALE,
        s(() -> new UiTxt("Player scale")),
        s(() -> new UiTxt("Controls your scale. This affects your actual hitbox.")),
        s(() -> new UiTxt("Values represent multiples of the default player model size.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> INTERACTION_DISTANCE = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.INTERACTION_DISTANCE,
        s(() -> new UiTxt("Interaction distance")),
        s(() -> new UiTxt("Controls your reach distance.")),
        s(() -> new UiTxt("Values represent the maximum distance you can interact with blocks and entities at, measured in Blocks.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> INTERACTION_RADIUS = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.INTERACTION_RADIUS,
        s(() -> new UiTxt("Interaction radius")),
        s(() -> new UiTxt("Controls your interaction radius. This lets you place, break or use multiple blocks at once.")),
        s(() -> new UiTxt("Values represent the maximum distance from your targeted position in which blocks respond to left and right clicks.")),
        s(() -> (UiTxt)new UiTxt("This stacks with [Interaction count].\n").green()),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> INTERACTION_COUNT = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.INTERACTION_COUNT,
        s(() -> new UiTxt("Interaction count")),
        s(() -> new UiTxt("Controls your interaction count. This lets you place, break, or use multiple blocks at once.")),
        s(() -> new UiTxt("Values represent the amount of times your interactions are processed. 1 is Vanilla's default.")),
        s(() -> (UiTxt)new UiTxt("This stacks with [Interaction radius].\n").green()),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> PLACEMENT_DELAY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.PLACEMENT_DELAY,
        s(() -> new UiTxt("Placement delay")),
        s(() -> new UiTxt("Controls the delay after which a new block is placed when holding the use button.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> AUTOCLICKER_DELAY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.AUTOCLICKER_DELAY,
        s(() -> new UiTxt("Autoclicker delay")),
        s(() -> new UiTxt("Controls the delay between interactions of the [Autoclicker] feature.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> AUTOCLICKER = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.AUTOCLICKER,
        s(() -> new UiTxt("Autoclicker")),
        s(() -> new UiTxt("Makes holding mouse buttons re-trigger the interaction every [Interaction delay].")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> TOGGLE_CLICKS = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.TOGGLE_CLICKS,
        s(() -> new UiTxt("Toggle clicks")),
        s(() -> new UiTxt("Lets you toggle mouse clicks when on.")),
        s(() -> (UiTxt)new UiTxt("This is compatible with the [Autoclicker] feature.\n").green()),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> NO_SIGN_GUI = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.NO_SIGN_GUI,
        s(() -> new UiTxt("Suppress Sign GUI")),
        s(() -> new UiTxt("Stops the Edit Sign GUI from showing up after placing a Sign or Hanging Sign.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_PICKING_UP_ITEMS = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_PICKING_UP_ITEMS,
        s(() -> new UiTxt("Disable picking up Items")),
        s(() -> new UiTxt("Stops you from picking up item entities.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> OPEN_OBSTRUCTED_CONTAINERS = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.OPEN_OBSTRUCTED_CONTAINERS,
        s(() -> new UiTxt("Open obstructed containers")),
        s(() -> new UiTxt("Lets you open Chests, Trapped Chests, Ender Chests, and Copper Chests even when they are below a solid block.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE,
        Notices.MULTIPLAYER_NOTICE //TODO actually check if this is not possible in multiplayer
    );




    public static final ClientFeature<?> PHASE_THROUGH_BLOCKS_FLY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.PHASE_THROUGH_BLOCKS_FLY,
        s(() -> new UiTxt("Phase through blocks")),
        s(() -> new UiTxt("Lets you phase through blocks while flying, completely ignoring their collisions.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE,
        Notices.MULTIPLAYER_NOTICE
    );
    public static final ClientFeature<?> PHASE_THROUGH_ENTITIES = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.PHASE_THROUGH_ENTITIES,
        s(() -> new UiTxt("Phase through entities")),
        s(() -> new UiTxt("Stops you from pushing and being pushed by other entities, even while not flying.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE,
        Notices.MULTIPLAYER_NOTICE
    );
    public static final ClientFeature<?> DISABLE_FIRE_EFFECT = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_FIRE_EFFECT,
        s(() -> new UiTxt("Disable being on fire")),
        s(() -> new UiTxt("Stops you from being on fire while standing in Fire, Soul Fire, Lava, Lava Cauldrons, Campfires and Soul Campfires.")),
        s(() -> new UiTxt("This also disables the Fire overlay that is normally shown when the player is on fire.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_FREEZING_EFFECT = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_FREEZING_EFFECT,
        s(() -> new UiTxt("Disable freezing")),
        s(() -> new UiTxt("Stops you from freezing while inside Powder Snow.")),
        s(() -> new UiTxt("This also disables the Freezing overlay and the FOV decrease that are normally applied when the player is freezing.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> FIX_HONEY_JUMP = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.FIX_HONEY_JUMP,
        s(() -> new UiTxt("Fix Honey Block jump")),
        s(() -> new UiTxt("Lets you jump while standing on Honey Blocks.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_HONEY_SLIDING = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_HONEY_SLIDING,
        s(() -> new UiTxt("Disable Honey Block sliding")),
        s(() -> new UiTxt("Stops you from sticking to the sides of Honey Blocks while falling or jumping, effectively removing their custom sliding physics.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_SLIME_BOUNCE = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_SLIME_BOUNCE,
        s(() -> new UiTxt("Disable bouncing on Slime Blocks")),
        s(() -> new UiTxt("Stops you from bouncing on Slime Blocks.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_BED_BOUNCE = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_BED_BOUNCE,
        s(() -> new UiTxt("Disable bouncing on beds")),
        s(() -> new UiTxt("Stops you from bouncing on Beds.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_ICE_SLIDING = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_ICE_SLIDING,
        s(() -> new UiTxt("Disable sliding on ice")),
        s(() -> new UiTxt("Stops you from sliding on Frosted Ice, Ice, Packed Ice, and Blue Ice.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_CURRENT_DRAG = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_CURRENT_DRAG,
        s(() -> new UiTxt("Disable fluid current drag")),
        s(() -> new UiTxt("Stops you from being moved by Water and Lava currents.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_BUBBLE_COLUMN_DRAG = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_BUBBLE_COLUMN_DRAG,
        s(() -> new UiTxt("Disable Bubble Column drag")),
        s(() -> new UiTxt("Stops you from being pushed up or pulled down while standing in Bubble Columns.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );




    public static final ClientFeature<?> DISABLE_HONEY_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_HONEY_SLOWDOWN,
        s(() -> new UiTxt("Disable Honey Block slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking on Honey Blocks.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_SLIME_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_SLIME_SLOWDOWN,
        s(() -> new UiTxt("Disable Slime Block slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking on Slime Blocks.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_SOULSAND_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_SOULSAND_SLOWDOWN,
        s(() -> new UiTxt("Disable Soul Sand slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking on Soul Sand.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_POWDER_SNOW_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_POWDER_SNOW_SLOWDOWN,
        s(() -> new UiTxt("Disable Powder Snow slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking through Powder Snow.")),
        s(() -> new UiTxt("This also lets you jump freely while inside Powder Snow.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_WATER_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_WATER_SLOWDOWN,
        s(() -> new UiTxt("Disable Water slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking through Water.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_LAVA_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_LAVA_SLOWDOWN,
        s(() -> new UiTxt("Disable Lava slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking through Lava.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_COBWEB_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_COBWEB_SLOWDOWN,
        s(() -> new UiTxt("Disable Cobweb slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking through Cobwebs.")),
        s(() -> new UiTxt("This also lets you jump freely while inside Cobwebs.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_LADDER_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_LADDER_SLOWDOWN,
        s(() -> new UiTxt("Disable Ladder slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking near Ladders.")),
        s(() -> new UiTxt("This doesn't let you jump while inside Ladders: Being a Climbable block, the jump button initiates the Climb action.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_VINES_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_VINES_SLOWDOWN,
        s(() -> new UiTxt("Disable Vines slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking near Vines.")),
        s(() -> new UiTxt("This doesn't let you jump while inside Vines: Being a Climbable block, the jump button initiates the Climb action.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_TWISTING_VINES_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_TWISTING_VINES_SLOWDOWN,
        s(() -> new UiTxt("Disable Twisting Vines slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking through Twisting Vines.")),
        s(() -> new UiTxt("This doesn't let you jump while inside Twisting Vines: Being a Climbable block, the jump button initiates the Climb action.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_WEEPING_VINES_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_WEEPING_VINES_SLOWDOWN,
        s(() -> new UiTxt("Disable Weeping Vines slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking through Weeping Vines.")),
        s(() -> new UiTxt("This doesn't let you jump while inside Weeping Vines: Being a Climbable block, the jump button initiates the Climb action.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_SWEET_BERRIES_SLOWDOWN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_SWEET_BERRIES_SLOWDOWN,
        s(() -> new UiTxt("Disable Sweet Berry Bush slowdown")),
        s(() -> new UiTxt("Prevents you from being slowed down while walking through Sweet Berry Bushes.")),
        s(() -> new UiTxt("This also lets you jump freely while inside Sweet Berry Bushes.")),
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    );




    public static final ClientFeature<?> DISABLE_BLOCK_BREAK_PARTICLES = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_BLOCK_BREAK_PARTICLES,
        s(() -> new UiTxt("Disable block break particles")),
        s(() -> new UiTxt("Disables the particles that are normally shown when breaking a block.")),
        Notices.CREATIVE_MODE_NOTICE
    );
    public static final ClientFeature<?> DISABLE_ITEM_CHANGE_ANIMATION = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_ITEM_CHANGE_ANIMATION,
        s(() -> new UiTxt("Disable item change animation")),
        s(() -> new UiTxt("Disables the animation that plays when you select a different item or the item you are holding changes.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_HAND_SWING_ANIMATION = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_HAND_SWING_ANIMATION,
        s(() -> new UiTxt("Disable hand swing animation")),
        s(() -> new UiTxt("Disables the animation that plays when you interact with or attack blocks or entities, or use certain items.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_DIMENSION_CHANGE_SCREEN = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_DIMENSION_CHANGE_SCREEN,
        s(() -> new UiTxt("Disable dimension change screen")),
        s(() -> new UiTxt("Disables the loading screen that appears when changing dimensions.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_WATER_FOV_CHANGE = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_WATER_FOV_CHANGE,
        s(() -> new UiTxt("Disable Water FOV")),
        s(() -> new UiTxt("Stops you from having reduced FOV while inside Water.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_WATER_OVERLAY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_WATER_OVERLAY,
        s(() -> new UiTxt("Disable overlay effect of Water")),
        s(() -> new UiTxt("Disables the overlay and tint effects that appear while inside Water.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_LAVA_OVERLAY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_LAVA_OVERLAY,
        s(() -> new UiTxt("Disable overlay effect of Lava")),
        s(() -> new UiTxt("Disables the overlay and tint effects that appear while inside Lava.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );
    public static final ClientFeature<?> DISABLE_NETHER_PORTAL_OVERLAY = new ClientFeature<>(
        CreativeTweaksServerFeatureSet.DISABLE_NETHER_PORTAL_OVERLAY,
        s(() -> new UiTxt("Disable Nether Portal overlay")),
        s(() -> new UiTxt("Disables the overlay effect that appears while inside Nether Portal blocks.")),
        Notices.CREATIVE_MODE_PLAYERS_UNAFFECTED_NOTICE
    );




    private class Notices {
        public static final Supplier<UiTxt> CREATIVE_MODE_NOTICE = () -> (UiTxt)new UiTxt(
            "This only works while in Creative Mode."
        ).yellow();

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
