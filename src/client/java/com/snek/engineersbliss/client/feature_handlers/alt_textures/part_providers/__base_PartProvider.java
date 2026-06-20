package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_PartProvider {
    public abstract Block getBlock();
    public abstract @Nullable List<String> calcPartNames(BlockState state);
    public abstract boolean shouldKeepVanilla(BlockState state);
}