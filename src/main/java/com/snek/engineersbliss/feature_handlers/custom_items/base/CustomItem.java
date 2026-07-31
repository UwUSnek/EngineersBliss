package com.snek.engineersbliss.feature_handlers.custom_items.base;

import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemProperties;

import net.minecraft.world.item.Item;




/**
 * The base class of custom items.
 */
public class CustomItem extends Item implements __base_CustomItem {
    private final boolean fullBright;
    public boolean isFullBright() { return fullBright; }


    public CustomItem(CustomItemProperties p) {
        this(p, false);
    }
    public CustomItem(CustomItemProperties p, final boolean fullBright) {
        super(p);
        this.fullBright = fullBright;
    }
}
