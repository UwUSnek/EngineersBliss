package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.ArrayList;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;



/**
 * A list of possible models for a specific block's BlockState, aka as a Model Set.
 * Each model is a list of baked model parts (Minecraft's BlockStateModel) to be rendered.
 */
public class ModelSet extends ArrayList<ArrayList<BlockStateModel>> {}
