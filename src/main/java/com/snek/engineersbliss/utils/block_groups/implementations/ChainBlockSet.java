package com.snek.engineersbliss.utils.block_groups.implementations;

import com.snek.engineersbliss.utils.block_groups.base.CopperBlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class ChainBlockSet extends CopperBlockSet {
    private Block iron;


    public Block iron() { return iron; }


    public ChainBlockSet() {
        super(
            //? if <=26.1.2 {
                /*Blocks.COPPER_CHAIN.unaffected(),
                Blocks.COPPER_CHAIN.exposed(),
                Blocks.COPPER_CHAIN.weathered(),
                Blocks.COPPER_CHAIN.oxidized(),
                Blocks.COPPER_CHAIN.waxed(),
                Blocks.COPPER_CHAIN.waxedExposed(),
                Blocks.COPPER_CHAIN.waxedWeathered(),
                Blocks.COPPER_CHAIN.waxedOxidized()
            *///? } else {
                Blocks.COPPER_CHAIN.weathering().unaffected(),
                Blocks.COPPER_CHAIN.weathering().exposed(),
                Blocks.COPPER_CHAIN.weathering().weathered(),
                Blocks.COPPER_CHAIN.weathering().oxidized(),
                Blocks.COPPER_CHAIN.waxed().unaffected(),
                Blocks.COPPER_CHAIN.waxed().exposed(),
                Blocks.COPPER_CHAIN.waxed().weathered(),
                Blocks.COPPER_CHAIN.waxed().oxidized()
            //? }
        );
        this.iron = register(Blocks.IRON_CHAIN);
    }
}
