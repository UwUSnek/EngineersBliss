package com.snek.engineersbliss.custom.items.special;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.custom.items.CustomItemProperties;
import com.snek.engineersbliss.custom.items.base.CustomBlockItem;
import com.snek.engineersbliss.mixin.accessors.BlockItemAccessor;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;





public class CustomHalfBlockItem extends CustomBlockItem {
    private final boolean isBottom;
    protected boolean isBottom() { return isBottom; }


    public CustomHalfBlockItem(Block block, CustomItemProperties p, final boolean isBottom) {
        super(block, p, null);
        this.isBottom = isBottom;
    }

    public CustomHalfBlockItem(Block block, CustomItemProperties p, final boolean isBottom, final @Nullable List<Block> mappedBlocks) {
        super(block, p, mappedBlocks);
        this.isBottom = isBottom;
    }



    // Force set half type on the placed block
    //! Also build the context from scratch. Vanilla's context takes into account the size of the
    //! full bed/door/plant block and doesn't allow placement where it wouldn't fit.
    @Override
    protected BlockState getPlacementState(final BlockPlaceContext context) {
        BlockState state = buildCustomPlacementState(context);
        if(state != null) {
            if(state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
                state = state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, isBottom ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER);
            }
            return state;
        }
        return state;
    }

    protected @Nullable BlockState buildCustomPlacementState(final BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockState state = getBlock().defaultBlockState();
        if(state.hasProperty(HorizontalDirectionalBlock.FACING)) {
            state = state.setValue(HorizontalDirectionalBlock.FACING, facing);
        }
        return canPlace(context, state) ? state : null;
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
