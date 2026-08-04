package com.snek.engineersbliss.feature_handlers.custom_items.special;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemProperties;
import com.snek.engineersbliss.feature_handlers.custom_items.base.CustomBlockItem;
import com.snek.engineersbliss.mixin.accessors.BlockItemAccessor;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;





public class CustomBedHalfBlockItem extends CustomHalfBlockItem {
    private boolean isFoot() { return isBottom(); }


    public CustomBedHalfBlockItem(Block block, CustomItemProperties p, final boolean isFoot) {
        super(block, p, isFoot);
    }

    public CustomBedHalfBlockItem(Block block, CustomItemProperties p, final boolean isFoot, final @Nullable List<Block> mappedBlocks) {
        super(block, p, isFoot, mappedBlocks);
    }



    // Force set half type on the placed block
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if(state != null && state.hasProperty(BedBlock.PART)) {
            state = state.setValue(BedBlock.PART, isFoot() ? BedPart.FOOT : BedPart.HEAD);
        }
        return state;
    }




    //! Inherit fixed place method from CustomHalfBlockItem
}
