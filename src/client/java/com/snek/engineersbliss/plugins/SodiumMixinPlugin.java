package com.snek.engineersbliss.plugins;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.snek.engineersbliss.EngineerSBliss;

import net.fabricmc.loader.api.FabricLoader;




public class SodiumMixinPlugin implements IMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
        if(mixinClassName.equals("com.snek.engineersbliss.client.mixin.rendering.sodium.RenderingFilterBlockSodiumMixin")) {
            return FabricLoader.getInstance().isModLoaded("sodium");
        }
        return true;
    }


    @Override public void onLoad(final String mixinPackage) {
        EngineerSBliss.LOGGER.info("Sodium detected. Using Sodium rendering filters");
    }


    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) { /* Empty */ }
    @Override public List<String> getMixins() { return Collections.emptyList(); }
    @Override public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) { /* Empty */ }
    @Override public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) { /* Empty */ }
}