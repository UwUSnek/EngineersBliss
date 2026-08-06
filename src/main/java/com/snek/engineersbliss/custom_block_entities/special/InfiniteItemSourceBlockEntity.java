package com.snek.engineersbliss.custom_block_entities.special;

import com.snek.engineersbliss.custom_block_entities.CustomBlockEntityHandler;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;




public class InfiniteItemSourceBlockEntity extends BlockEntity {

    private ItemVariant sourceItem = ItemVariant.of(Items.COBBLESTONE);
    private final SingleSlotStorage<ItemVariant> storage = new SingleSlotStorage<>() {
        @Override public ItemVariant getResource() { return sourceItem; }
        @Override public long getAmount() { return Long.MAX_VALUE; }
        @Override public long getCapacity() { return Long.MAX_VALUE; }
        @Override public boolean isResourceBlank() { return sourceItem.isBlank(); }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            return 0; // Don't accept anything
        }


        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            return resource.equals(sourceItem) ? maxAmount : 0;
        }
    };


    public InfiniteItemSourceBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntityHandler.INFINITE_ITEM_SOURCE, pos, state);
    }


    public Storage<ItemVariant> getStorage() { return storage; }


    public void setSourceItem(ItemVariant item) {
        sourceItem = item;
        setChanged();
    }
}