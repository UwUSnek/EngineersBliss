package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;




@Mixin(LivingEntity.class)
public abstract class FluidSpeedMixin {

    @Shadow abstract void jumpOutOfFluid(final double oldY);
    @Shadow abstract void travelInAir(final Vec3 input);
    @Shadow abstract void floatInWaterWhileRidden();
	@Shadow abstract double getEffectiveGravity();




    /**
     * Replaces water movement with air movement to keep correct speed and disable water related movement buffs.
     * Manually adds back the floating in fluid, falling in fluid, and jumping out of fluid physics.
     */
    @Unique private void customTravelInFluid(final Player _this, final Vec3 input, final double baseGravity, final boolean isFalling, final double oldY) {

        // Default air movement
        // ! Force set onGround to true while running this to stop fluids from making the player "not on the ground" while sprinting.
        // ! No idea why they do that, but they do, and that changes how block friction is calculated, making movements feel slippery.
        final boolean lastOnGround = _this.onGround();
        _this.setOnGround(true);
        travelInAir(input);
        _this.setOnGround(lastOnGround);


        //! Reset gravity back to water gravity
        Vec3 mov = _this.getDeltaMovement();
        _this.setDeltaMovement(new Vec3(mov.x, mov.y + getEffectiveGravity(), mov.z));


        // Apply floating in fluid, falling in fluid, and jumping in fluid physics
        _this.setDeltaMovement(_this.getFluidFallingAdjustedMovement(baseGravity, isFalling, _this.getDeltaMovement()));
        floatInWaterWhileRidden();
        jumpOutOfFluid(oldY);
    }




    @SuppressWarnings("unused")
    @Inject(method = "travelInWater", at = @At("HEAD"), cancellable = true, require = 1)
    private void travelInWater(final Vec3 input, final double baseGravity, final boolean isFalling, final double oldY, final CallbackInfo ci) {

        // If entity is a player and they are not swimming (keep default swimming movement) and they have the feature active, use the custom movement
        if((Object)this instanceof Player _this) {
            if(!_this.isSwimming()) {
                if(CreativeTweaksHandler.clientPlayerHasFeature(this, CreativeTweakFeature.DISABLE_WATER_SLOWDOWN)) {
                    customTravelInFluid(_this, input, baseGravity, isFalling, oldY);
                    ci.cancel();
                }
            }
        }
    }




    @SuppressWarnings("unused")
    @Inject(method = "travelInLava", at = @At("HEAD"), cancellable = true, require = 1)
    private void travelInLava(final Vec3 input, final double baseGravity, final boolean isFalling, final double oldY, final CallbackInfo ci) {

        // If entity is a player and they have the feature active, use the custom movement
        if((Object)this instanceof Player _this) {
            if(CreativeTweaksHandler.clientPlayerHasFeature(this, CreativeTweakFeature.DISABLE_LAVA_SLOWDOWN)) {
                customTravelInFluid(_this, input, baseGravity, isFalling, oldY);
                ci.cancel();
            }
        }
    }
}