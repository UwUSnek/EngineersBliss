package com.snek.engineersbliss.utils.block_groups.implementations;

import com.snek.engineersbliss.utils.block_groups.base.ColoredBlockSet;

import net.minecraft.world.level.block.Blocks;



public class BedBlockSet extends ColoredBlockSet {
    public BedBlockSet() {
        super(
            //? if <=26.1.2 {
                /*Blocks.WHITE_BED,
                Blocks.ORANGE_BED,
                Blocks.MAGENTA_BED,
                Blocks.LIGHT_BLUE_BED,
                Blocks.YELLOW_BED,
                Blocks.LIME_BED,
                Blocks.PINK_BED,
                Blocks.GRAY_BED,
                Blocks.LIGHT_GRAY_BED,
                Blocks.CYAN_BED,
                Blocks.PURPLE_BED,
                Blocks.BLUE_BED,
                Blocks.BROWN_BED,
                Blocks.GREEN_BED,
                Blocks.RED_BED,
                Blocks.BLACK_BED
            *///? } else {
                Blocks.BED.white(),
                Blocks.BED.orange(),
                Blocks.BED.magenta(),
                Blocks.BED.lightBlue(),
                Blocks.BED.yellow(),
                Blocks.BED.lime(),
                Blocks.BED.pink(),
                Blocks.BED.gray(),
                Blocks.BED.lightGray(),
                Blocks.BED.cyan(),
                Blocks.BED.purple(),
                Blocks.BED.blue(),
                Blocks.BED.brown(),
                Blocks.BED.green(),
                Blocks.BED.red(),
                Blocks.BED.black()
            //? }
        );
    }
}
