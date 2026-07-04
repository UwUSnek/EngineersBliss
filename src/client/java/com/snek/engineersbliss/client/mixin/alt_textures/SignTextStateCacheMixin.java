package com.snek.engineersbliss.client.mixin.alt_textures;

import com.snek.engineersbliss.client.utils.SignTextStateCacheAccess;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignText;




@Mixin(SignText.class)
public abstract class SignTextStateCacheMixin implements SignTextStateCacheAccess {

    @Unique private boolean engineersbliss$dirty = true;
    @Unique private boolean engineersbliss$cache;

    @Shadow public abstract Component[] getMessages(boolean filtered);

    @Override
    public boolean engineersbliss$hasText() {
        if (engineersbliss$dirty) {
            engineersbliss$cache = compute();
            engineersbliss$dirty = false;
        }
        return engineersbliss$cache;
    }

    @Unique
    private boolean compute() {
        for(final var msg : getMessages(false)) {
            if(msg.visit(s -> s.isEmpty() ? Optional.<Boolean>empty() : Optional.of(Boolean.TRUE)).isPresent()) return true;
        }
        return false;
    }
}