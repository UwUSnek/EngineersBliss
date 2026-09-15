package com.snek.engineersbliss.utils.block_groups;

import com.snek.engineersbliss.utils.block_groups.implementations.BannerBlockSet;
import com.snek.engineersbliss.utils.block_groups.implementations.BedBlockSet;
import com.snek.engineersbliss.utils.block_groups.implementations.ChainBlockSet;
import com.snek.engineersbliss.utils.block_groups.implementations.ChestBlockSet;
import com.snek.engineersbliss.utils.block_groups.implementations.CopperGolemStatueBlockSet;
import com.snek.engineersbliss.utils.block_groups.implementations.LanternBlockSet;
import com.snek.engineersbliss.utils.block_groups.implementations.MetalBarsBlockSet;
import com.snek.engineersbliss.utils.block_groups.implementations.SignBlockSet;








/**
 * A class containing sets of blocks that is usable during static initialization.
 * ! This MUST be updated manually as new blocks are added to affected categories.
 * ! This is required because Minecraft's tag registries are not available during static initialization.
 *
 * ! It also doubles as version-safe block sets.
 * ! The way blocks are gatecorized changes between Minecraft versions. Using this makes the code more readable.
 */
public class BlockSets {
    private BlockSets() {}

    public static final LanternBlockSet           LANTERN             = new LanternBlockSet();
    public static final SignBlockSet              SIGN                = new SignBlockSet();
    public static final ChestBlockSet             CHEST               = new ChestBlockSet();
    public static final CopperGolemStatueBlockSet COPPER_GOLEM_STATUE = new CopperGolemStatueBlockSet();
    public static final MetalBarsBlockSet         METAL_BARS          = new MetalBarsBlockSet();
    public static final ChainBlockSet             CHAIN               = new ChainBlockSet();
    public static final BedBlockSet               BED                 = new BedBlockSet();
    public static final BannerBlockSet            BANNER              = new BannerBlockSet();
}