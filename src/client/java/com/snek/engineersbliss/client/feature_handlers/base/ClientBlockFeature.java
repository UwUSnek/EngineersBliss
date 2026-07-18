package com.snek.engineersbliss.client.feature_handlers.base;

import java.util.List;
import java.util.function.Supplier;

import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.world.level.block.Block;




/**
 * A ClientFeature that keeps track of the blocks it affects the static rendering of.
 * ! This is used to refresh chunk sections and optimize branches in hot paths.
 */
public class ClientBlockFeature<F extends __base_ServerFeature<?>> extends ClientFeature<F> {
    private final List<Block> affectedBlocks;


    public List<Block> getAffectedBlocks() { return affectedBlocks; }


    public ClientBlockFeature(F serverFeature, Supplier<Txt> nameSupplier, Supplier<Txt> descSupplier, final List<Block> affectedBlocks) {
        super(serverFeature, nameSupplier, descSupplier);
        this.affectedBlocks = affectedBlocks;
    }

}
