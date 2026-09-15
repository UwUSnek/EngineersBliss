package com.snek.engineersbliss.utils.block_groups.implementations;

import com.snek.engineersbliss.utils.block_groups.base.CopperBlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class LanternBlockSet extends CopperBlockSet {
    private Block normal;
    private Block soul;


    public Block normal() { return normal; }
    public Block   soul() { return soul;   }


    public LanternBlockSet() {
        super(
            //? if <=26.1.2 {
                /*Blocks.COPPER_LANTERN.unaffected(),
                Blocks.COPPER_LANTERN.exposed(),
                Blocks.COPPER_LANTERN.weathered(),
                Blocks.COPPER_LANTERN.oxidized(),
                Blocks.COPPER_LANTERN.waxed(),
                Blocks.COPPER_LANTERN.waxedExposed(),
                Blocks.COPPER_LANTERN.waxedWeathered(),
                Blocks.COPPER_LANTERN.waxedOxidized()
            *///? } else {
                Blocks.COPPER_LANTERN.weathering().unaffected(),
                Blocks.COPPER_LANTERN.weathering().exposed(),
                Blocks.COPPER_LANTERN.weathering().weathered(),
                Blocks.COPPER_LANTERN.weathering().oxidized(),
                Blocks.COPPER_LANTERN.waxed().unaffected(),
                Blocks.COPPER_LANTERN.waxed().exposed(),
                Blocks.COPPER_LANTERN.waxed().weathered(),
                Blocks.COPPER_LANTERN.waxed().oxidized()
            //? }
        );
        this.normal = register(Blocks.LANTERN);
        this.soul   = register(Blocks.SOUL_LANTERN);
    }
}
