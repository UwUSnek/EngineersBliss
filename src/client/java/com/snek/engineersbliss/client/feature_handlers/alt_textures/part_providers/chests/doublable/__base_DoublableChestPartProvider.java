package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.doublable;

import java.util.ArrayList;
import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.__base_ChestPartProvider;

import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;




public abstract class __base_DoublableChestPartProvider extends __base_ChestPartProvider {

    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String chestName = getChestName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(ChestBlock.FACING));
        final String sideName = switch(state.getValue(ChestBlock.TYPE)) {
            case ChestType.SINGLE -> "single";
            case ChestType.LEFT   -> "left";
            case ChestType.RIGHT  -> "right";
        };
        return List.of(String.format("chests/static/%s/%s%s", sideName, chestName, dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String chestName = getChestName();
        final List<String> r = new ArrayList<>();
        r.add(String.format("chests/static/left/%s",  chestName));
        r.add(String.format("chests/static/right/%s", chestName));
        r.addAll(super.calcDependencyNames());
        return r;
    }
}