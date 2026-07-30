package com.snek.engineersbliss.feature_handlers.custom_items.special;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemProperties;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;




/**
 * The base class of custom block items with standing and wall variants.
 * This can stop the item registration phase from overwriting vanilla (& modded) block-to-item mappings and define custom mappings.
 * Vanilla block items have no parameter for that.
 */
public class CustomStandingAndWallBlockItem extends StandingAndWallBlockItem {
    final @Nullable List<Block> mappedBlocks;


    public CustomStandingAndWallBlockItem(Block block, final Block wallBlock, final Direction attachmentDirection, CustomItemProperties p, final @Nullable List<Block> mappedBlocks) {
        super(block, wallBlock, attachmentDirection, p);
        this.mappedBlocks = mappedBlocks;
    }


    @Override
    public void registerBlocks(final Map<Block, Item> map, final Item item) {

        // Register custom block mappings if present
        if(mappedBlocks != null) {
            for(final Block mappedBlock : mappedBlocks) {
                map.put(mappedBlock, item);
            }
        }

        // Otherwise, override registerBlocks with a no-op so the existing mapping is preserved.
        else {
            // Empty
        }
    }
}
