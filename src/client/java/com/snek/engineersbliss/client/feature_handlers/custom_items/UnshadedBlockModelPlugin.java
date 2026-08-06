package com.snek.engineersbliss.client.feature_handlers.custom_items;

import java.util.HashMap;
import java.util.Map;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.custom_items.CustomBlockHandler;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;




public class UnshadedBlockModelPlugin implements ModelLoadingPlugin {
    private static final Map<Block, UnshadedBlockStateModel> MODELS = new HashMap<>();

    public static void register(final Block block, final Identifier spriteId) {
        MODELS.put(block, new UnshadedBlockStateModel(spriteId));
    }

    static {
        register(
            CustomBlockHandler.GREEN_SCREEN,
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "block/custom/green_screen")
        );
        register(
            CustomBlockHandler.BLUE_SCREEN,
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "block/custom/blue_screen")
        );
    }

    @Override
    public void initialize(final Context pluginContext) {
        pluginContext.modifyBlockModelAfterBake().register((vanilla, ctx) -> {
            final UnshadedBlockStateModel custom = MODELS.get(ctx.state().getBlock());
            return custom != null ? custom : vanilla;
        });
    }
}