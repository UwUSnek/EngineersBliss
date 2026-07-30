package com.snek.engineersbliss.client.feature_handlers.overlays.providers.alternative_invisible_blocks;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.TextureProviderDisplay;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.__base_TextureOverlayProvider;
import com.snek.engineersbliss.feature_handlers.overlays.OverlaysServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




public final class StructureVoidOverlayProvider extends __base_TextureOverlayProvider {


    @Override
    public boolean shouldRender(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        final Player player = Minecraft.getInstance().player;
        if(player == null) return false;
        return
            ClientFeatureSync.getFeatureB(OverlaysServerFeatureSet.BETTER_STRUCTURE_VOID_DISPLAY) &&
            state.is(Blocks.STRUCTURE_VOID) &&
            player.getMainHandItem().getItem() == (Items.STRUCTURE_VOID)
        ;
    }

    @Override
    public String calcTexturePath(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return "overlays/invisible_blocks/structure_void/block.png";
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

