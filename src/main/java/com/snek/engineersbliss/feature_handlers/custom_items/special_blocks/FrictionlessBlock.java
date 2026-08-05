package com.snek.engineersbliss.feature_handlers.custom_items.special_blocks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.mojang.serialization.MapCodec;




public class FrictionlessBlock extends Block {
    public static final MapCodec<FrictionlessBlock> CODEC = simpleCodec(FrictionlessBlock::new);

    public FrictionlessBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }



    /**
     * Called once per Entity.move().
     * This resets the friction calculations Vanilla does so entities can keep sliding forever.
     */
    @Override
    public void updateEntityMovementAfterFallOn(final BlockGetter level, final Entity entity) {
        double x = entity.getDeltaMovement().x;
        double z = entity.getDeltaMovement().z;

        if(entity.onGround()) {
            float blockFriction = this.getFriction();

            if(entity instanceof LivingEntity livingEntity && !livingEntity.shouldDiscardFriction()) {
                float compensation = 1.0F / (blockFriction * 0.91F);
                x *= compensation;
                z *= compensation;
            }
            else if(entity instanceof ItemEntity) {
                float compensation = 1.0F / (blockFriction * 0.98F);
                x *= compensation;
                z *= compensation;
            }
        }


        // Vanilla zeroes vertical velocity on landing. This does the same
        entity.setDeltaMovement(x, 0.0, z);

        // Force the server to keep sending motion update packets to clients
        entity.needsSync = true;
    }
}