package com.snek.engineersbliss.client.feature_handlers.overlays.providers.alternative_invisible_blocks;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.TextureProviderDisplay;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.__base_TextureOverlayProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




public final class BarrierOverlayProvider extends __base_TextureOverlayProvider {


    @Override
    public boolean shouldRender(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return
            OverlaysHandler.getFeature(OverlayFeature.BETTER_BARRIER_DISPLAY) &&
            state.is(Blocks.BARRIER) &&
            Minecraft.getInstance().player.getMainHandItem().getItem() == (Items.BARRIER)
        ;
    }

    @Override
    public String calcTexturePath(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return "overlays/invisible_blocks/barrier/block.png";
    }

    @Override
    public double calcVerticalOffset(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return 0.5;
    }

    @Override
    public double calcWidth(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return 0.5;
    }

    @Override
    public TextureProviderDisplay getDisplay() {
        return TextureProviderDisplay.BILLBOARD;
    }

    @Override
    public float calcAnchor(BlockState state, BlockPos pos, __base_OverlayAttachedData data) {
        return 0.5f;
    }
}

