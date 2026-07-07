package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.ArrayList;
import java.util.List;

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
    public List<String> calcPartNames(final BlockState state) {
        final List<String> r = new ArrayList<>();
        final boolean is3d = AltTexturesHandler.getFeature(AltTextureFeature.REDSTONE_WIRE_3D);
        final String wireModelDir = String.format("redstone_wire/minimal/%sd", is3d ? "3" : "2");

        final RedstoneSide n = state.getValue(RedStoneWireBlock.NORTH);
        final RedstoneSide e = state.getValue(RedStoneWireBlock.EAST);
        final RedstoneSide s = state.getValue(RedStoneWireBlock.SOUTH);
        final RedstoneSide w = state.getValue(RedStoneWireBlock.WEST);


        // Central dot
        if(n == RedstoneSide.NONE && e == RedstoneSide.NONE && s == RedstoneSide.NONE && w == RedstoneSide.NONE) {
            r.add(String.format("%s/large_dot_n", wireModelDir));
        }
        else if(
            n != RedstoneSide.NONE && e != RedstoneSide.NONE ||
            e != RedstoneSide.NONE && s != RedstoneSide.NONE ||
            s != RedstoneSide.NONE && w != RedstoneSide.NONE ||
            w != RedstoneSide.NONE && n != RedstoneSide.NONE
        ) r.add(String.format("%s/dot_n", wireModelDir));

        // Side connections
        if(n == RedstoneSide.SIDE) r.add(String.format("%s/down_n", wireModelDir));
        if(e == RedstoneSide.SIDE) r.add(String.format("%s/down_e", wireModelDir));
        if(s == RedstoneSide.SIDE) r.add(String.format("%s/down_s", wireModelDir));
        if(w == RedstoneSide.SIDE) r.add(String.format("%s/down_w", wireModelDir));
        if(n == RedstoneSide.UP)   r.add(String.format("%s/up_n", wireModelDir));
        if(e == RedstoneSide.UP)   r.add(String.format("%s/up_e", wireModelDir));
        if(s == RedstoneSide.UP)   r.add(String.format("%s/up_s", wireModelDir));
        if(w == RedstoneSide.UP)   r.add(String.format("%s/up_w", wireModelDir));

        return r;
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return
            AltTexturesHandler.getFeature(AltTextureFeature.MINIMAL_REDSTONE_WIRE) ||
            AltTexturesHandler.getFeature(AltTextureFeature.REDSTONE_WIRE_3D)
        ;
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}
