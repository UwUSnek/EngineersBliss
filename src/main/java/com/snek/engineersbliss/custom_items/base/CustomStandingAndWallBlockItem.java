package com.snek.engineersbliss.custom_items.base;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.custom_items.CustomItemProperties;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;




/**
 * The base class of custom block items with standing and wall variants.
 * This can stop the item registration phase from overwriting vanilla (& modded) block-to-item mappings and define custom mappings.
 * Vanilla block items have no parameter for that.
 */
public class CustomStandingAndWallBlockItem extends StandingAndWallBlockItem implements __base_CustomBlockItem {
    private final @Nullable List<Block> mappedBlocks;
    private final boolean fullBright;
    public boolean isFullBright() { return fullBright; }


    public CustomStandingAndWallBlockItem(Block block, final Block wallBlock, final Direction attachmentDirection, CustomItemProperties p, final @Nullable List<Block> mappedBlocks) {
        this(block, wallBlock, attachmentDirection, p, mappedBlocks, false);
    }
    public CustomStandingAndWallBlockItem(Block block, final Block wallBlock, final Direction attachmentDirection, CustomItemProperties p, final @Nullable List<Block> mappedBlocks, final boolean fullBright) {
        super(block, wallBlock, attachmentDirection, p);
        this.mappedBlocks = mappedBlocks;
        this.fullBright = fullBright;
    }


    @Override
    public void registerBlocks(final Map<Block, Item> map, final Item item) {
        __base_CustomBlockItem.super.registerBlocks(map, item, mappedBlocks);
    }
}
