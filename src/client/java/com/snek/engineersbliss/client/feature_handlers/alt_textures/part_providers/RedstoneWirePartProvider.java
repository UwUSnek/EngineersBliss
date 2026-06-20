package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RedstoneSide;




public class RedstoneWirePartProvider extends __base_PartProvider {


    @Override
    public Block getBlock() {
        return Blocks.REDSTONE_WIRE;
    }




    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {

        final boolean isMinimal = AltTexturesHandler.getFeature(AltTextureFeature.MINIMAL_REDSTONE_WIRE);
        final boolean is3d = AltTexturesHandler.getFeature(AltTextureFeature.REDSTONE_WIRE_3D);
        if(isMinimal || is3d) {
            final List<String> r = new ArrayList<>();
            final RedstoneSide n = state.getValue(RedStoneWireBlock.NORTH);
            final RedstoneSide e = state.getValue(RedStoneWireBlock.EAST);
            final RedstoneSide s = state.getValue(RedStoneWireBlock.SOUTH);
            final RedstoneSide w = state.getValue(RedStoneWireBlock.WEST);
            final String wireModelDir = "redstone_wire/minimal/" + (is3d ? "3d" : "2d");

            // Central dot and power level
            if(
                n == RedstoneSide.NONE && e == RedstoneSide.NONE && s == RedstoneSide.NONE && w == RedstoneSide.NONE ||
                n != RedstoneSide.NONE && e != RedstoneSide.NONE ||
                e != RedstoneSide.NONE && s != RedstoneSide.NONE ||
                s != RedstoneSide.NONE && w != RedstoneSide.NONE ||
                w != RedstoneSide.NONE && n != RedstoneSide.NONE
            ) r.add(wireModelDir + "/dot_n");

            // Side connections
            if(n == RedstoneSide.SIDE) r.add(wireModelDir + "/down_n");
            if(e == RedstoneSide.SIDE) r.add(wireModelDir + "/down_e");
            if(s == RedstoneSide.SIDE) r.add(wireModelDir + "/down_s");
            if(w == RedstoneSide.SIDE) r.add(wireModelDir + "/down_w");
            if(n == RedstoneSide.UP)   r.add(wireModelDir + "/up_n");
            if(e == RedstoneSide.UP)   r.add(wireModelDir + "/up_e");
            if(s == RedstoneSide.UP)   r.add(wireModelDir + "/up_s");
            if(w == RedstoneSide.UP)   r.add(wireModelDir + "/up_w");

            return r;
        }
        else {
            return null;
        }
    }




    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return
            ! AltTexturesHandler.getFeature(AltTextureFeature.MINIMAL_REDSTONE_WIRE) &&
            ! AltTexturesHandler.getFeature(AltTextureFeature.REDSTONE_WIRE_3D)
        ;
    }
}
