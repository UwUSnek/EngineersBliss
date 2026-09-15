package com.snek.engineersbliss.utils.block_groups.implementations;

import com.snek.engineersbliss.utils.block_groups.base.CopperBlockSet;

import net.minecraft.world.level.block.Blocks;




public class CopperGolemStatueBlockSet extends CopperBlockSet {
    public CopperGolemStatueBlockSet() {
        super(
            //? if <=26.1.2 {
                /*Blocks.COPPER_GOLEM_STATUE,
                Blocks.EXPOSED_COPPER_GOLEM_STATUE,
                Blocks.WEATHERED_COPPER_GOLEM_STATUE,
                Blocks.OXIDIZED_COPPER_GOLEM_STATUE,
                Blocks.WAXED_COPPER_GOLEM_STATUE,
                Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE,
                Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE,
                Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE
            *///? } else {
                Blocks.COPPER_GOLEM_STATUE.weathering().unaffected(),
                Blocks.COPPER_GOLEM_STATUE.weathering().exposed(),
                Blocks.COPPER_GOLEM_STATUE.weathering().weathered(),
                Blocks.COPPER_GOLEM_STATUE.weathering().oxidized(),
                Blocks.COPPER_GOLEM_STATUE.waxed().unaffected(),
                Blocks.COPPER_GOLEM_STATUE.waxed().exposed(),
                Blocks.COPPER_GOLEM_STATUE.waxed().weathered(),
                Blocks.COPPER_GOLEM_STATUE.waxed().oxidized()
            //? }
        );
    }
}
