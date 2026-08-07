package com.snek.engineersbliss.client.mixin.alt_textures.renderer_suppressors;

import com.snek.engineersbliss.client.utils.SignTextStateCacheAccess;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignText;




/**
 * This mixin caches information about the sign text.
 * Specifically, it calculates whether the text is empty, and stores that in the text instance.
 */
@Mixin(SignText.class)
public abstract class SignTextStateCacheMixin implements SignTextStateCacheAccess {

    @Unique private boolean eb$dirty = true;
    @Unique private boolean eb$cache;

    @Shadow public abstract Component[] getMessages(boolean filtered);

    @Override
    public boolean eb$hasText() {
        if(eb$dirty) {
            eb$cache = eb$compute();
            eb$dirty = false;
        }
        return eb$cache;
    }

    @Unique
    private boolean eb$compute() {
        for(final var msg : getMessages(false)) {
            if(msg.visit(s -> s.isEmpty() ? Optional.<Boolean>empty() : Optional.of(Boolean.TRUE)).isPresent()) return true;
        }
        return false;
    }
}