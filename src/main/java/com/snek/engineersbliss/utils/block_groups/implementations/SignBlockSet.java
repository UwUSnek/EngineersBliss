package com.snek.engineersbliss.utils.block_groups.implementations;

import com.snek.engineersbliss.utils.block_groups.base.CompositeBlockSet;
import com.snek.engineersbliss.utils.block_groups.base.WoodenBlockSet;

import net.minecraft.world.level.block.Blocks;





public class SignBlockSet extends CompositeBlockSet {
    public static class HangingSignTypeBlockSet extends CompositeBlockSet {
        private final WoodenBlockSet ceiling;
        private final WoodenBlockSet wall;
        public WoodenBlockSet ceiling() { return ceiling; }
        public WoodenBlockSet    wall() { return wall;    }
        public HangingSignTypeBlockSet(final WoodenBlockSet ceilingSet, final WoodenBlockSet wallSet) {
            super(ceilingSet, wallSet);
            this.ceiling = ceilingSet;
            this.wall    = wallSet;
        }
    }
    public static class StandingSignTypeBlockSet extends CompositeBlockSet {
        private final WoodenBlockSet floor;
        private final WoodenBlockSet wall;
        public WoodenBlockSet floor() { return floor; }
        public WoodenBlockSet  wall() { return wall;  }
        public StandingSignTypeBlockSet(final WoodenBlockSet floorSet, final WoodenBlockSet wallSet) {
            super(floorSet, wallSet);
            this.floor = floorSet;
            this.wall  = wallSet;
        }
    }



    private final HangingSignTypeBlockSet  hanging;
    private final StandingSignTypeBlockSet standing;

    public HangingSignTypeBlockSet   hanging() { return hanging;  }
    public StandingSignTypeBlockSet standing() { return standing; }




    public SignBlockSet() {
        final HangingSignTypeBlockSet  hangingSet = new HangingSignTypeBlockSet(
            new WoodenBlockSet(
                Blocks.OAK_HANGING_SIGN,
                Blocks.SPRUCE_HANGING_SIGN,
                Blocks.BIRCH_HANGING_SIGN,
                Blocks.JUNGLE_HANGING_SIGN,
                Blocks.ACACIA_HANGING_SIGN,
                Blocks.DARK_OAK_HANGING_SIGN,
                Blocks.MANGROVE_HANGING_SIGN,
                Blocks.CHERRY_HANGING_SIGN,
                Blocks.PALE_OAK_HANGING_SIGN,
                Blocks.BAMBOO_HANGING_SIGN,
                Blocks.CRIMSON_HANGING_SIGN,
                Blocks.WARPED_HANGING_SIGN
            ),
            new WoodenBlockSet(
                Blocks.OAK_WALL_HANGING_SIGN,
                Blocks.SPRUCE_WALL_HANGING_SIGN,
                Blocks.BIRCH_WALL_HANGING_SIGN,
                Blocks.JUNGLE_WALL_HANGING_SIGN,
                Blocks.ACACIA_WALL_HANGING_SIGN,
                Blocks.DARK_OAK_WALL_HANGING_SIGN,
                Blocks.MANGROVE_WALL_HANGING_SIGN,
                Blocks.CHERRY_WALL_HANGING_SIGN,
                Blocks.PALE_OAK_WALL_HANGING_SIGN,
                Blocks.BAMBOO_WALL_HANGING_SIGN,
                Blocks.CRIMSON_WALL_HANGING_SIGN,
                Blocks.WARPED_WALL_HANGING_SIGN
            )
        );
        final StandingSignTypeBlockSet standingSet = new StandingSignTypeBlockSet(
            new WoodenBlockSet(
                Blocks.OAK_SIGN,
                Blocks.SPRUCE_SIGN,
                Blocks.BIRCH_SIGN,
                Blocks.JUNGLE_SIGN,
                Blocks.ACACIA_SIGN,
                Blocks.DARK_OAK_SIGN,
                Blocks.MANGROVE_SIGN,
                Blocks.CHERRY_SIGN,
                Blocks.PALE_OAK_SIGN,
                Blocks.BAMBOO_SIGN,
                Blocks.CRIMSON_SIGN,
                Blocks.WARPED_SIGN
            ),
            new WoodenBlockSet(
                Blocks.OAK_WALL_SIGN,
                Blocks.SPRUCE_WALL_SIGN,
                Blocks.BIRCH_WALL_SIGN,
                Blocks.JUNGLE_WALL_SIGN,
                Blocks.ACACIA_WALL_SIGN,
                Blocks.DARK_OAK_WALL_SIGN,
                Blocks.MANGROVE_WALL_SIGN,
                Blocks.CHERRY_WALL_SIGN,
                Blocks.PALE_OAK_WALL_SIGN,
                Blocks.BAMBOO_WALL_SIGN,
                Blocks.CRIMSON_WALL_SIGN,
                Blocks.WARPED_WALL_SIGN
            )
        );
        super(hangingSet, standingSet);
        this.hanging  = hangingSet;
        this.standing = standingSet;
    }
}
