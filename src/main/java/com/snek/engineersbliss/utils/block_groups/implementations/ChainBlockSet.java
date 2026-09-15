package com.snek.engineersbliss.utils.block_groups.implementations;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.block_groups.base.CopperBlockSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class ChainBlockSet extends __base_BlockSet {
    public Map<String, Block> byTypeAndWaxAndOxidationState() { return byProperty; }


    private Block iron;
    private CopperBlockSet copper;


    public Block iron() { return iron; }
    public CopperBlockSet copper() { return copper; }


    public ChainBlockSet() {
        this.copper = new CopperBlockSet(
            //? if <=26.1.2 {
                /*Blocks.COPPER_CHAIN.unaffected    (),
                Blocks.COPPER_CHAIN.exposed       (),
                Blocks.COPPER_CHAIN.weathered     (),
                Blocks.COPPER_CHAIN.oxidized      (),
                Blocks.COPPER_CHAIN.waxed         (),
                Blocks.COPPER_CHAIN.waxedExposed  (),
                Blocks.COPPER_CHAIN.waxedWeathered(),
                Blocks.COPPER_CHAIN.waxedOxidized ()
            *///? } else {
                Blocks.COPPER_CHAIN.weathering().unaffected(),
                Blocks.COPPER_CHAIN.weathering().exposed   (),
                Blocks.COPPER_CHAIN.weathering().weathered (),
                Blocks.COPPER_CHAIN.weathering().oxidized  (),
                Blocks.COPPER_CHAIN.waxed     ().unaffected(),
                Blocks.COPPER_CHAIN.waxed     ().exposed   (),
                Blocks.COPPER_CHAIN.waxed     ().weathered (),
                Blocks.COPPER_CHAIN.waxed     ().oxidized  ()
            //? }
        );
        for(final @NotNull var e : copper.byWaxAndOxidationState().entrySet()) registerWithCustomData("copper_"  + e.getKey(), e.getValue());
        this.iron = registerWithCustomData("iron", Blocks.IRON_CHAIN);
    }
}
