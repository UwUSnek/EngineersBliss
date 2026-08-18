package com.snek.engineersbliss.custom.block_entities.special;

import com.snek.engineersbliss.custom.block_entities.CustomBlockEntityHandler;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;




public class ItemPipeBlockEntity extends BlockEntity {

    private final SingleSlotStorage<ItemVariant> storage = new SingleSlotStorage<>() {
        @Override public ItemVariant getResource() { return ItemVariant.blank(); }
        @Override public long getAmount() { return 0; }
        @Override public long getCapacity() { return Long.MAX_VALUE; }
        @Override public boolean isResourceBlank() { return true; }


        // Accept and discard everything //FIXME
        @Override public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            return maxAmount;
        }


        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            return 0; //FIXME
        }
    };


    public ItemPipeBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntityHandler.ITEM_PIPE, pos, state);
    }


    public Storage<ItemVariant> getStorage() { return storage; }
}