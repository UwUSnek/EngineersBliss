package com.snek.engineersbliss.utils.block_groups.implementations;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.block_groups.base.ColoredBlockSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;








public class BannerBlockSet extends __base_BlockSet {
    public Map<String, Block> byTypeAndColor() { return byProperty; }


    private final ColoredBlockSet floor;
    private final ColoredBlockSet wall;


    public ColoredBlockSet floor() { return floor; }
    public ColoredBlockSet  wall() { return wall;  }


    public BannerBlockSet() {
        //? if <=26.1.2 {
            /*final ColoredBlockSet floorSet = new ColoredBlockSet(
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
            final ColoredBlockSet wallSet = new ColoredBlockSet(
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
            final ColoredBlockSet floorSet = new ColoredBlockSet(
                Blocks.BANNER.white    (),
                Blocks.BANNER.orange   (),
                Blocks.BANNER.magenta  (),
                Blocks.BANNER.lightBlue(),
                Blocks.BANNER.yellow   (),
                Blocks.BANNER.lime     (),
                Blocks.BANNER.pink     (),
                Blocks.BANNER.gray     (),
                Blocks.BANNER.lightGray(),
                Blocks.BANNER.cyan     (),
                Blocks.BANNER.purple   (),
                Blocks.BANNER.blue     (),
                Blocks.BANNER.brown    (),
                Blocks.BANNER.green    (),
                Blocks.BANNER.red      (),
                Blocks.BANNER.black    ()
            );
            final ColoredBlockSet wallSet = new ColoredBlockSet(
                Blocks.WALL_BANNER.white    (),
                Blocks.WALL_BANNER.orange   (),
                Blocks.WALL_BANNER.magenta  (),
                Blocks.WALL_BANNER.lightBlue(),
                Blocks.WALL_BANNER.yellow   (),
                Blocks.WALL_BANNER.lime     (),
                Blocks.WALL_BANNER.pink     (),
                Blocks.WALL_BANNER.gray     (),
                Blocks.WALL_BANNER.lightGray(),
                Blocks.WALL_BANNER.cyan     (),
                Blocks.WALL_BANNER.purple   (),
                Blocks.WALL_BANNER.blue     (),
                Blocks.WALL_BANNER.brown    (),
                Blocks.WALL_BANNER.green    (),
                Blocks.WALL_BANNER.red      (),
                Blocks.WALL_BANNER.black    ()
            );
        //? }
        for(final @NotNull var e : floorSet.byColor().entrySet()) registerWithCustomData("floor_" + e.getKey(), e.getValue());
        for(final @NotNull var e :  wallSet.byColor().entrySet()) registerWithCustomData("wall_"  + e.getKey(), e.getValue());
        this.floor = floorSet;
        this.wall  = wallSet;
    }

}