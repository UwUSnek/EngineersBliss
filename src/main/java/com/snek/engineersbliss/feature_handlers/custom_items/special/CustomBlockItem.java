package com.snek.engineersbliss.feature_handlers.custom_items.special;

import java.util.Map;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;




/**
 * The base class of custom block items.
 * This stops the item registration phase from overwriting vanilla (& modded) block-to-item mappings.
 */
public class CustomBlockItem extends BlockItem {


    public CustomBlockItem(Block block, Properties p) {
        super(block, p);
    }


    //! Override registerBlocks with a no-op so the existing mapping is preserved.
    @Override
    public void registerBlocks(final Map<Block, Item> map, final Item item) {
        // Empty
    }
}
