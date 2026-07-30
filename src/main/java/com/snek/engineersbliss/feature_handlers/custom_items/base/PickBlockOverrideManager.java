package com.snek.engineersbliss.feature_handlers.custom_items.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;




/**
 * A class that keeps track of custom pick block overrides.
 * ! Vanilla blocks all individually decide what to return when picked, so writing a mixin for each of them isn't practical.
 * ! This does it all at the cost of being limited to select usages instead of anything the vanilla method is used for.
 */
public class PickBlockOverrideManager {
    private PickBlockOverrideManager() {}
    private static final Map<Block, Item> PICK_OVERRIDES = new HashMap<>();
    public static @Nullable Item getOverride(final Block block) {
        return PICK_OVERRIDES.get(block);
    }


    /**
     * Registers the provided overrides in the vanilla Map and a local overrides map.
     * @param vanillaMap The vanilla map to update. This is the same map passed to Item.registerBlocks.
     * @param blocks The blocks to associare to the item.
     * @param item The item the blocks are to be associated to.
     */
    public static void registerCustomOverrides(final @NotNull Map<Block, Item> vanillaMap, final @NotNull List<Block> blocks, final @NotNull Item item) {
        for(final Block mappedBlock : blocks) {
            vanillaMap    .put(mappedBlock, item);
            PICK_OVERRIDES.put(mappedBlock, item);
        }
    }
}