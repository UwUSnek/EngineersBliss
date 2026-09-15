package com.snek.engineersbliss.utils.block_groups.implementations;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.block_groups.base.CopperBlockSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class ChestBlockSet extends __base_BlockSet {
    public Map<String, Block> byTypeAndWaxAndOxidationState() { return byProperty; }


    private Block normal;
    private Block trapped;
    private Block ender;
    private CopperBlockSet copper;


    public Block  normal() { return normal;  }
    public Block trapped() { return trapped; }
    public Block   ender() { return ender;   }
    public CopperBlockSet copper() { return copper; }


    public ChestBlockSet() {
        this.copper = new CopperBlockSet(
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
                Blocks.COPPER_CHEST.weathering().exposed   (),
                Blocks.COPPER_CHEST.weathering().weathered (),
                Blocks.COPPER_CHEST.weathering().oxidized  (),
                Blocks.COPPER_CHEST.waxed     ().unaffected(),
                Blocks.COPPER_CHEST.waxed     ().exposed   (),
                Blocks.COPPER_CHEST.waxed     ().weathered (),
                Blocks.COPPER_CHEST.waxed     ().oxidized  ()
            //? }
        );
        for(final @NotNull var e : copper.byWaxAndOxidationState().entrySet()) registerWithCustomData("copper_"  + e.getKey(), e.getValue());
        this.normal  = registerWithCustomData("normal",  Blocks.CHEST);
        this.trapped = registerWithCustomData("trapped", Blocks.TRAPPED_CHEST);
        this.ender   = registerWithCustomData("ender",   Blocks.ENDER_CHEST);
    }
}
