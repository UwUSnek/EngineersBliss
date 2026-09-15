package com.snek.engineersbliss.utils.block_groups.implementations;

import com.snek.engineersbliss.utils.block_groups.base.CopperBlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class MetalBarsBlockSet extends CopperBlockSet {
    private Block iron;


    public Block iron() { return iron; }


    public MetalBarsBlockSet() {
        super(
            //? if <=26.1.2 {
                /*Blocks.COPPER_BARS.unaffected(),
                Blocks.COPPER_BARS.exposed(),
                Blocks.COPPER_BARS.weathered(),
                Blocks.COPPER_BARS.oxidized(),
                Blocks.COPPER_BARS.waxed(),
                Blocks.COPPER_BARS.waxedExposed(),
                Blocks.COPPER_BARS.waxedWeathered(),
                Blocks.COPPER_BARS.waxedOxidized()
            *///? } else {
                Blocks.COPPER_BARS.weathering().unaffected(),
                Blocks.COPPER_BARS.weathering().exposed(),
                Blocks.COPPER_BARS.weathering().weathered(),
                Blocks.COPPER_BARS.weathering().oxidized(),
                Blocks.COPPER_BARS.waxed().unaffected(),
                Blocks.COPPER_BARS.waxed().exposed(),
                Blocks.COPPER_BARS.waxed().weathered(),
                Blocks.COPPER_BARS.waxed().oxidized()
            //? }
        );
        this.iron = register(Blocks.IRON_BARS);
    }
}
