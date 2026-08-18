package com.snek.engineersbliss.custom.block_entities.base;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;








public class CosmeticBlackAndWhiteHoleBlockEntity extends BlockEntity {
    public static final float DEFAULT_SIZE   = 1.0f;
    public static final float DEFAULT_GROWTH = 1.001f;

    private float size;
    private float growth;
    public void setSize(final float newSize) {
        size = newSize;
        setChanged();
    }
    public float getSize() {
        return size;
    }
    public void setGrowth(final float newGrowth) {
        growth = newGrowth;
        setChanged();
    }
    public float getGrowth() {
        return growth;
    }




    public CosmeticBlackAndWhiteHoleBlockEntity(BlockEntityType<? extends CosmeticBlackAndWhiteHoleBlockEntity> type, BlockPos pos, BlockState state) {
        this(type, pos, state, DEFAULT_SIZE, DEFAULT_GROWTH);
    }
    public CosmeticBlackAndWhiteHoleBlockEntity(BlockEntityType<? extends CosmeticBlackAndWhiteHoleBlockEntity> type, BlockPos pos, BlockState state, final float size, final float growth) {
        super(type, pos, state);
        this.size = size;
        this.growth = growth;
    }


    public static void tick(Level level, BlockPos pos, BlockState state, CosmeticBlackAndWhiteHoleBlockEntity entity) {
        if(entity.getGrowth() != 1f) {
            entity.setSize(entity.getSize() * entity.getGrowth());
        }
    }




    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putFloat("Size", size);
        output.putFloat("Growth", growth);
    }
    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        size   = input.getFloatOr("Size",   DEFAULT_SIZE);
        growth = input.getFloatOr("Growth", DEFAULT_GROWTH);
    }
    @Override
    public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public net.minecraft.nbt.CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }
}
