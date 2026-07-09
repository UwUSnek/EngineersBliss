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
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;




public final class LightBlockOverlayProvider extends __base_TextureOverlayProvider {


    @Override
    public boolean shouldRender(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return
            OverlaysHandler.getFeature(OverlayFeature.BETTER_LIGHT_BLOCK_DISPLAY) &&
            state.is(Blocks.LIGHT) &&
            Minecraft.getInstance().player.getMainHandItem().getItem() == (Items.LIGHT)
        ;
    }

    @Override
    public String calcTexturePath(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        final int lightLevel = state.getValue(LightBlock.LEVEL);
        return "overlays/invisible_blocks/light_block/" + lightLevel + ".png";
    }

    @Override
    public double calcVerticalOffset(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return 0.5;
    }

    @Override
    public double calcWidth(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return 0.5;
    }

    @Override
    public TextureProviderDisplay getDisplay() {
        return TextureProviderDisplay.BILLBOARD;
    }

    @Override
    public float calcAnchor(final BlockState state, final BlockPos pos, final __base_OverlayAttachedData data) {
        return 0.5f;
    }
}

