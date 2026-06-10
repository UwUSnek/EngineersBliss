package com.snek.engineersbliss.client.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;




public class RenderFilterHandler {
    private RenderFilterHandler() { }


    private static final Map<Block, Boolean> enabledStates  = new HashMap<>();
    private static final Map<Block, Boolean> isolatedStates = new HashMap<>();
    public static void initMaps() {
        BuiltInRegistries.BLOCK.forEach(block -> {
            setEnabled(block, true);
            setIsolated(block, false);
        });
        recalculate();
    }

    public static void  setEnabled(final Block block, final boolean  enabled) { enabledStates.put(block, enabled); }
    public static void setIsolated(final Block block, final boolean isolated) { isolatedStates.put(block, isolated); }
    public static boolean  getEnabled(final Block block) { return enabledStates.get(block); }
    public static boolean getIsolated(final Block block) { return isolatedStates.get(block); }




    private static final List<Block> activeBlocks = new ArrayList<>();
    public static List<Block> getActiveBlocks() { return activeBlocks; }

    public static void recalculate() {
        activeBlocks.clear();
        for(Entry<Block, Boolean> entry : isolatedStates.entrySet()) {
            if(entry.getValue().booleanValue()) activeBlocks.add(entry.getKey());
        }
        if(activeBlocks.isEmpty()) for(Entry<Block, Boolean> entry : enabledStates.entrySet()) {
            if(entry.getValue().booleanValue()) activeBlocks.add(entry.getKey());
        }
    }
}
