package com.snek.engineersbliss.client.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;








/**
 * A class providing utility functions for block entity handling.
 * BlockEntityUtils
 */
public class BlockEntityUtils {
    private BlockEntityUtils() {}




    /**
     * Checks if the provided lectern block entity is holding a book.
     * @param blockEntity The lectern block entity.
     * @return True if the lectern contains a book, false otherwise.
     */
    public static boolean campfireHasItems(final LecternBlockEntity blockEntity) {
        return blockEntity.getBlockState().getValue(LecternBlock.HAS_BOOK);
    }



    /**
     * Checks if the provided campfire block entity is holding items.
     * @param blockEntity The campfire block entity.
     * @return True if the campfire contains items, false otherwise.
     */
    public static boolean campfireHasItems(final CampfireBlockEntity blockEntity) {
        for(final ItemStack item : blockEntity.getItems()) if(!item.isEmpty()) return true;
        return false;
    }




    /**
     * Checks if the provided shelf block entity is holding items.
     * @param blockEntity The shelf block entity.
     * @return True if the shelf contains items, false otherwise.
     */
    public static boolean shelfHasItems(final ShelfBlockEntity blockEntity) {
        for(final ItemStack item : blockEntity.getItems()) if(!item.isEmpty()) return true;
        return false;
    }




    /**
     * Checks if the provided sign block entity contains any text at all.
     * ! SignText already provides a hasMessage method, but it's not optimal.
     * ! This is a manual implementation used for computational speed.
     * @param blockEntity The sign block entity.
     * @return True if the sign contains text, false otherwise.
     */
    public static boolean signHasText(final SignBlockEntity blockEntity) {
        return
            ((SignTextStateCacheAccess)(Object) blockEntity.getFrontText()).engineersbliss$hasText() ||
            ((SignTextStateCacheAccess)(Object) blockEntity.getBackText ()).engineersbliss$hasText()
        ;
    }




    /**
     * Checks if the provided banner block entity contains custom pattern layers.
     * @param blockEntity The banner block entity.
     * @return True if the banner contains patterns, false otherwise.
     */
    public static boolean bannerHasPatterns(final BannerBlockEntity blockEntity) {
        return !blockEntity.getPatterns().layers().isEmpty();
    }




    /**
     * Checks if the provided decorated pot block entity has at least one side decorated with a Pottery Sherd.
     * @param blockEntity The decorated pot block entity.
     * @return True if the pot has at least one pottery sherd, false otherwise.
     */
    public static boolean decoratedPotHasSherds(final DecoratedPotBlockEntity blockEntity) {
        return decoratedPotHasSherds(blockEntity.getDecorations());
    }
    public static boolean decoratedPotHasSherds(final PotDecorations decorations) {
        return
            !decorations.front().isEmpty() ||
            !decorations.back ().isEmpty() ||
            !decorations.right().isEmpty() ||
            !decorations.left ().isEmpty()
        ;
    }
}
