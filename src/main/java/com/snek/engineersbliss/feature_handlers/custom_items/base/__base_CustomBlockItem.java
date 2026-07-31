package com.snek.engineersbliss.feature_handlers.custom_items.base;

import com.snek.engineersbliss.feature_handlers.custom_items.PickBlockOverrideManager;

import java.util.List;
import java.util.Map;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;




/**
 * The base class of all custom block items (fake multiple inheritance for Java).
 **/
public interface __base_CustomBlockItem extends __base_CustomItem {
    public default void registerBlocks(final Map<Block, Item> map, final Item item, final List<Block> mappedBlocks) {

        // Register custom block mappings if present
        if(mappedBlocks != null) {
            for(final Block mappedBlock : mappedBlocks) {
                map.put(mappedBlock, item);
            }
            PickBlockOverrideManager.registerCustomOverrides(map, mappedBlocks, item);
        }

        // Otherwise, override registerBlocks with a no-op so the existing mapping is preserved.
        else { /* Empty */ }
    }
}
