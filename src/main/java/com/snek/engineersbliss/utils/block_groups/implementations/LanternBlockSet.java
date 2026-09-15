package com.snek.engineersbliss.utils.block_groups.implementations;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.block_groups.base.CopperBlockSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class LanternBlockSet extends __base_BlockSet {
    public Map<String, Block> byTypeAndWaxAndOxidationState() { return byProperty; }


    private Block normal;
    private Block soul;
    private CopperBlockSet copper;


    public Block normal() { return normal; }
    public Block   soul() { return soul;   }
    public CopperBlockSet copper() { return copper; }


    public LanternBlockSet() {
        this.copper = new CopperBlockSet(
            //? if <=26.1.2 {
                /*Blocks.COPPER_LANTERN.unaffected    (),
                Blocks.COPPER_LANTERN.exposed       (),
                Blocks.COPPER_LANTERN.weathered     (),
                Blocks.COPPER_LANTERN.oxidized      (),
                Blocks.COPPER_LANTERN.waxed         (),
                Blocks.COPPER_LANTERN.waxedExposed  (),
                Blocks.COPPER_LANTERN.waxedWeathered(),
                Blocks.COPPER_LANTERN.waxedOxidized ()
            *///? } else {
                Blocks.COPPER_LANTERN.weathering().unaffected(),
                Blocks.COPPER_LANTERN.weathering().exposed   (),
                Blocks.COPPER_LANTERN.weathering().weathered (),
                Blocks.COPPER_LANTERN.weathering().oxidized  (),
                Blocks.COPPER_LANTERN.waxed     ().unaffected(),
                Blocks.COPPER_LANTERN.waxed     ().exposed   (),
                Blocks.COPPER_LANTERN.waxed     ().weathered (),
                Blocks.COPPER_LANTERN.waxed     ().oxidized  ()
            //? }
        );
        for(final @NotNull var e : copper.byWaxAndOxidationState().entrySet()) registerWithCustomData("copper_"  + e.getKey(), e.getValue());
        this.normal = registerWithCustomData("normal", Blocks.LANTERN);
        this.soul   = registerWithCustomData("soul", Blocks.SOUL_LANTERN);
    }
}
