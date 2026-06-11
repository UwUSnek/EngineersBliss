package com.snek.engineersbliss.plugins;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;




public class SodiumMixinPlugin implements IMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(mixinClassName.equals("com.snek.engineersbliss.client.mixin.rendering.sodium.RenderFilterBlockSodiumMixin")) {
            return FabricLoader.getInstance().isModLoaded("sodium");
        }
        return true;
    }


    @Override public void onLoad(String mixinPackage) { /* Empty */ }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { /* Empty */ }
    @Override public List<String> getMixins() { return Collections.emptyList(); }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { /* Empty */ }
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { /* Empty */ }
}