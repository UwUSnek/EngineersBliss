package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base;

import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;




public abstract class __base_BlockSetPartProvider extends __base_PartProvider {
    protected final __base_BlockSet blockSet;
    public String getBlockResourceName() {
        return blockSet.byBlock().get(getTargetBlock());
    }


    protected __base_BlockSetPartProvider(Block targetBlock, final __base_BlockSet blockSet) {
        super(targetBlock);
        this.blockSet = blockSet;
    }
}
