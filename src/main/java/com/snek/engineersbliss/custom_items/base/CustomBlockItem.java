package com.snek.engineersbliss.custom_items.base;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.custom_items.CustomItemProperties;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;




/**
 * The base class of custom block items.
 * This can stop the item registration phase from overwriting vanilla (& modded) block-to-item mappings and define custom mappings.
 * Vanilla block items have no parameter for that.
 */
public class CustomBlockItem extends BlockItem implements __base_CustomBlockItem {
    private final @Nullable List<Block> mappedBlocks;
    private final boolean fullBright;
    public boolean isFullBright() { return fullBright; }


    public CustomBlockItem(Block block, CustomItemProperties p, final @Nullable List<Block> mappedBlocks) {
        this(block, p, mappedBlocks, false);
    }
    public CustomBlockItem(Block block, CustomItemProperties p, final @Nullable List<Block> mappedBlocks, final boolean fullBright) {
        super(block, p);
        this.mappedBlocks = mappedBlocks;
        this.fullBright = fullBright;
    }


    @Override
    public void registerBlocks(final Map<Block, Item> map, final Item item) {
        __base_CustomBlockItem.super.registerBlocks(map, item, mappedBlocks);
    }
}
