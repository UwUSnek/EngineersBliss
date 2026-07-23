package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.bars;

import java.util.ArrayList;
import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockState;




//! For whatever reason, Copper bars are a subclass of IronBarsBlock.
//! IronBarsBlock extends CrossCollisionBlock, which also includes glass panels and fences. This is where the properties are taken from.
public abstract class __base_BarsPartProvider extends __base_PartProvider {

    protected abstract String getMaterialName();




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String materialName = getMaterialName();
        final boolean n = state.getValue(CrossCollisionBlock.NORTH);
        final boolean e = state.getValue(CrossCollisionBlock.EAST);
        final boolean s = state.getValue(CrossCollisionBlock.SOUTH);
        final boolean w = state.getValue(CrossCollisionBlock.WEST);
        final int total = (n ? 1 : 0) + (e ? 1 : 0) + (s ? 1 : 0) + (w ? 1 : 0);


        //! Copy Vanilla's blockstate json logic
        final List<String> r = new ArrayList<>();
        /**/                r.add(String.format("bars/3d/post_ends/%s_n", materialName));
        if(total == 0)      r.add(String.format("bars/3d/post/%s_n",      materialName));
        if(total == 1 && n) r.add(String.format("bars/3d/cap/%s_n",       materialName));
        if(total == 1 && e) r.add(String.format("bars/3d/cap/%s_e",       materialName));
        if(total == 1 && s) r.add(String.format("bars/3d/cap_alt/%s_n",   materialName));
        if(total == 1 && w) r.add(String.format("bars/3d/cap_alt/%s_e",   materialName));
        if(n)               r.add(String.format("bars/3d/side/%s_n",      materialName));
        if(e)               r.add(String.format("bars/3d/side/%s_e",      materialName));
        if(s)               r.add(String.format("bars/3d/side_alt/%s_n",  materialName));
        if(w)               r.add(String.format("bars/3d/side_alt/%s_e",  materialName));
        return r;
    }
    @Override
    public List<String> calcDependencyNames() {
        final String materialName = getMaterialName();
        return List.of(
            String.format("bars/3d/cap/%s",       materialName),
            String.format("bars/3d/cap_alt/%s",   materialName),
            String.format("bars/3d/post/%s",      materialName),
            String.format("bars/3d/post_ends/%s", materialName),
            String.format("bars/3d/side/%s",      materialName),
            String.format("bars/3d/side_alt/%s",  materialName)
        );
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.BARS_3D);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}
