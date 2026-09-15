package com.snek.engineersbliss.utils.block_groups.implementations;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.block_groups.base.CopperBlockSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class MetalBarsBlockSet extends __base_BlockSet {
    public Map<String, Block> byTypeAndWaxAndOxidationState() { return byProperty; }


    private Block iron;
    private CopperBlockSet copper;


    public Block iron() { return iron; }
    public CopperBlockSet copper() { return copper; }


    public MetalBarsBlockSet() {
        this.copper = new CopperBlockSet(
            //? if <=26.1.2 {
                /*Blocks.COPPER_BARS.unaffected    (),
                Blocks.COPPER_BARS.exposed       (),
                Blocks.COPPER_BARS.weathered     (),
                Blocks.COPPER_BARS.oxidized      (),
                Blocks.COPPER_BARS.waxed         (),
                Blocks.COPPER_BARS.waxedExposed  (),
                Blocks.COPPER_BARS.waxedWeathered(),
                Blocks.COPPER_BARS.waxedOxidized ()
            *///? } else {
                Blocks.COPPER_BARS.weathering().unaffected(),
                Blocks.COPPER_BARS.weathering().exposed   (),
                Blocks.COPPER_BARS.weathering().weathered (),
                Blocks.COPPER_BARS.weathering().oxidized  (),
                Blocks.COPPER_BARS.waxed     ().unaffected(),
                Blocks.COPPER_BARS.waxed     ().exposed   (),
                Blocks.COPPER_BARS.waxed     ().weathered (),
                Blocks.COPPER_BARS.waxed     ().oxidized  ()
            //? }
        );
        for(final @NotNull var e : copper.byWaxAndOxidationState().entrySet()) registerWithCustomData("copper_"  + e.getKey(), e.getValue());
        this.iron = registerWithCustomData("iron", Blocks.IRON_BARS);
    }
}
