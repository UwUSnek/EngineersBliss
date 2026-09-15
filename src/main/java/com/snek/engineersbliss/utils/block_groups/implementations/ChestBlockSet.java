package com.snek.engineersbliss.utils.block_groups.implementations;

import com.snek.engineersbliss.utils.block_groups.base.CompositeBlockSet;
import com.snek.engineersbliss.utils.block_groups.base.CopperBlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class ChestBlockSet extends CopperBlockSet {
    private Block normal;
    private Block trapped;
    private Block ender;


    public Block  normal() { return normal;  }
    public Block trapped() { return trapped; }
    public Block   ender() { return ender;   }


    public ChestBlockSet() {
        super(
            //? if <=26.1.2 {
                /*Blocks.COPPER_CHEST,
                Blocks.EXPOSED_COPPER_CHEST,
                Blocks.WEATHERED_COPPER_CHEST,
                Blocks.OXIDIZED_COPPER_CHEST,
                Blocks.WAXED_COPPER_CHEST,
                Blocks.WAXED_EXPOSED_COPPER_CHEST,
                Blocks.WAXED_WEATHERED_COPPER_CHEST,
                Blocks.WAXED_OXIDIZED_COPPER_CHEST
            *///? } else {
                Blocks.COPPER_CHEST.weathering().unaffected(),
                Blocks.COPPER_CHEST.weathering().exposed(),
                Blocks.COPPER_CHEST.weathering().weathered(),
                Blocks.COPPER_CHEST.weathering().oxidized(),
                Blocks.COPPER_CHEST.waxed().unaffected(),
                Blocks.COPPER_CHEST.waxed().exposed(),
                Blocks.COPPER_CHEST.waxed().weathered(),
                Blocks.COPPER_CHEST.waxed().oxidized()
            //? }
        );
        this.normal  = register(Blocks.CHEST);
        this.trapped = register(Blocks.TRAPPED_CHEST);
        this.ender   = register(Blocks.ENDER_CHEST);
    }
}
