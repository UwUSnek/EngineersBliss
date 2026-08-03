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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;


//FIXME merge with CustomPlantHalfBlockItem
//FIXME use BlockStateProperties.DOUBLE_BLOCK_HALF


public class CustomDoorHalfBlockItem extends CustomBlockItem {
    private final boolean isBottom;

    public CustomDoorHalfBlockItem(Block block, CustomItemProperties p, final boolean isBottom) {
        super(block, p, null);
        this.isBottom = isBottom;
    }

    public CustomDoorHalfBlockItem(Block block, CustomItemProperties p, final boolean isBottom, final @Nullable List<Block> mappedBlocks) {
        super(block, p, mappedBlocks);
        this.isBottom = isBottom;
    }



    // Force set half type on the placed block
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if(state != null && state.hasProperty(DoorBlock.HALF)) {
            state = state.setValue(DoorBlock.HALF, isBottom ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER);
        }
        return state;
    }








    //! Stop Minecraft from placing 2 blocks at once
    /** Verbatim copy of Vanilla's place, without the part that places another block */
    @Override
    public InteractionResult place(BlockPlaceContext placeContext) {
        if(!this.getBlock().isEnabled(placeContext.getLevel().enabledFeatures()) || !placeContext.canPlace()) {
            return InteractionResult.FAIL;
        }

        BlockPlaceContext updatedPlaceContext = this.updatePlacementContext(placeContext);
        if(updatedPlaceContext == null) {
            return InteractionResult.FAIL;
        }

        BlockState placementState = this.getPlacementState(updatedPlaceContext);
        if(placementState == null) {
            return InteractionResult.FAIL;
        }

        if(!this.placeBlock(updatedPlaceContext, placementState)) {
            return InteractionResult.FAIL;
        }

        BlockPos pos = updatedPlaceContext.getClickedPos();
        Level level = updatedPlaceContext.getLevel();
        Player player = updatedPlaceContext.getPlayer();
        ItemStack itemStack = updatedPlaceContext.getItemInHand();
        BlockState placedState = level.getBlockState(pos);

        if(placedState.is(placementState.getBlock())) {
            placedState = ((BlockItemAccessor)this).invokeUpdateBlockStateFromTag(pos, level, itemStack, placedState);
            updateCustomBlockEntityTag(pos, level, player, itemStack, placedState);
            BlockItemAccessor.invokeUpdateBlockEntityComponents(level, pos, itemStack);
            if(player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, itemStack);
            }
        }

        SoundType soundType = placedState.getSoundType();
        level.playSound(player, pos, this.getPlaceSound(placedState), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, placedState));
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
