package com.snek.engineersbliss.custom.items.special;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.Block;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.custom.items.CustomItemProperties;
import com.snek.engineersbliss.custom.items.base.CustomStandingAndWallBlockItem;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.core.Direction;




public class YourPlayerHeadItem extends CustomStandingAndWallBlockItem {
    public YourPlayerHeadItem(Block standing, Block wall, Direction attachDirection, CustomItemProperties p, final @Nullable List<Block> mappedBlocks) {
        super(standing, wall, attachDirection, p, mappedBlocks);
    }


    @Override
	public void inventoryTick(final ItemStack itemStack, final ServerLevel level, final Entity owner, @Nullable final EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);
        if(owner instanceof final @NotNull Player player && !itemStack.has(DataComponents.PROFILE)) {
            itemStack.set(DataComponents.PROFILE, ResolvableProfile.createResolved(player.getGameProfile()));
            itemStack.set(DataComponents.CUSTOM_NAME, new Txt(player.getName()).cat("'s Head").yellow().noItalic().get());
        }
    }
}