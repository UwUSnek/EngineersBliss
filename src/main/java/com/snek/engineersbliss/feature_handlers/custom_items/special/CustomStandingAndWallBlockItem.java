package com.snek.engineersbliss.feature_handlers.custom_items.special;

import java.util.Map;

import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemProperties;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;




/**
 * The base class of custom block items with standing and wall variants.
 * This stops the item registration phase from overwriting vanilla (& modded) block-to-item mappings.
 */
public class CustomStandingAndWallBlockItem extends StandingAndWallBlockItem {


    public CustomStandingAndWallBlockItem(Block block, final Block wallBlock, final Direction attachmentDirection, CustomItemProperties p) {
        super(block, wallBlock, attachmentDirection, p);
    }


    //! Override registerBlocks with a no-op so the existing mapping is preserved.
    @Override
    public void registerBlocks(final Map<Block, Item> map, final Item item) {
        // Empty
    }
}
