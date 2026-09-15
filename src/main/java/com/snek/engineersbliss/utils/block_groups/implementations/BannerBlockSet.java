package com.snek.engineersbliss.utils.block_groups.implementations;

import com.snek.engineersbliss.utils.block_groups.base.ColouredBlockSet;
import com.snek.engineersbliss.utils.block_groups.base.CompositeBlockSet;

import net.minecraft.world.level.block.Blocks;




public class BannerBlockSet extends CompositeBlockSet {
    private final ColouredBlockSet floor;
    private final ColouredBlockSet wall;


    public ColouredBlockSet floor() { return floor; }
    public ColouredBlockSet  wall() { return wall;  }


    public BannerBlockSet() {
        //? if <=26.1.2 {
             /*final ColouredBlockSet floorSet = new ColouredBlockSet(
                 Blocks.WHITE_BANNER,
                 Blocks.ORANGE_BANNER,
                 Blocks.MAGENTA_BANNER,
                 Blocks.LIGHT_BLUE_BANNER,
                 Blocks.YELLOW_BANNER,
                 Blocks.LIME_BANNER,
                 Blocks.PINK_BANNER,
                 Blocks.GRAY_BANNER,
                 Blocks.LIGHT_GRAY_BANNER,
                 Blocks.CYAN_BANNER,
                 Blocks.PURPLE_BANNER,
                 Blocks.BLUE_BANNER,
                 Blocks.BROWN_BANNER,
                 Blocks.GREEN_BANNER,
                 Blocks.RED_BANNER,
                 Blocks.BLACK_BANNER
             );
             final ColouredBlockSet wallSet = new ColouredBlockSet(
                 Blocks.WHITE_WALL_BANNER,
                 Blocks.ORANGE_WALL_BANNER,
                 Blocks.MAGENTA_WALL_BANNER,
                 Blocks.LIGHT_BLUE_WALL_BANNER,
                 Blocks.YELLOW_WALL_BANNER,
                 Blocks.LIME_WALL_BANNER,
                 Blocks.PINK_WALL_BANNER,
                 Blocks.GRAY_WALL_BANNER,
                 Blocks.LIGHT_GRAY_WALL_BANNER,
                 Blocks.CYAN_WALL_BANNER,
                 Blocks.PURPLE_WALL_BANNER,
                 Blocks.BLUE_WALL_BANNER,
                 Blocks.BROWN_WALL_BANNER,
                 Blocks.GREEN_WALL_BANNER,
                 Blocks.RED_WALL_BANNER,
                 Blocks.BLACK_WALL_BANNER
             );
        *///? } else {
            final ColouredBlockSet floorSet = new ColouredBlockSet(
                Blocks.BANNER.white(),
                Blocks.BANNER.orange(),
                Blocks.BANNER.magenta(),
                Blocks.BANNER.lightBlue(),
                Blocks.BANNER.yellow(),
                Blocks.BANNER.lime(),
                Blocks.BANNER.pink(),
                Blocks.BANNER.gray(),
                Blocks.BANNER.lightGray(),
                Blocks.BANNER.cyan(),
                Blocks.BANNER.purple(),
                Blocks.BANNER.blue(),
                Blocks.BANNER.brown(),
                Blocks.BANNER.green(),
                Blocks.BANNER.red(),
                Blocks.BANNER.black()
            );
            final ColouredBlockSet wallSet = new ColouredBlockSet(
                Blocks.WALL_BANNER.white(),
                Blocks.WALL_BANNER.orange(),
                Blocks.WALL_BANNER.magenta(),
                Blocks.WALL_BANNER.lightBlue(),
                Blocks.WALL_BANNER.yellow(),
                Blocks.WALL_BANNER.lime(),
                Blocks.WALL_BANNER.pink(),
                Blocks.WALL_BANNER.gray(),
                Blocks.WALL_BANNER.lightGray(),
                Blocks.WALL_BANNER.cyan(),
                Blocks.WALL_BANNER.purple(),
                Blocks.WALL_BANNER.blue(),
                Blocks.WALL_BANNER.brown(),
                Blocks.WALL_BANNER.green(),
                Blocks.WALL_BANNER.red(),
                Blocks.WALL_BANNER.black()
            );
        //? }
        super(floorSet, wallSet);
        this.floor = floorSet;
        this.wall  = wallSet;
    }

}