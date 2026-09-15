package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.BlockSets;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;




public class ChestPartProvider extends __base_BlockSetPartProvider {
    public ChestPartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String chestName = getBlockResourceName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(ChestBlock.FACING));
        final String sideName = (state.hasProperty(ChestBlock.TYPE)) ? switch(state.getValue(ChestBlock.TYPE)) {
            case ChestType.SINGLE -> "single";
            case ChestType.LEFT   -> "left";
            case ChestType.RIGHT  -> "right";
        } : "single";
        return List.of(String.format("chests/static/%s/%s%s", sideName, chestName, dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String chestName = getBlockResourceName();
        if(getTargetBlock() == BlockSets.CHEST.ender()) return List.of(
            String.format("chests/static/single/%s", chestName)
        );
        else return List.of(
            String.format("chests/static/single/%s", chestName),
            String.format("chests/static/left/%s",   chestName),
            String.format("chests/static/right/%s",  chestName)
        );
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_CHESTS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}