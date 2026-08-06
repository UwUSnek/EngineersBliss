package com.snek.engineersbliss.custom.items;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;




/**
 * An Item.Properties that can define a custom path for the item's JSON file.
 * ! Item.Properties has methods to set the custom path, but for whatever reason
 * ! IT JUST IGNORES THE COMPONENT AND SETS THE PATH TO THE ITEM ID, COMPLETELY DISREGARDING
 * ! SUBDIRECTORY LEVELS AND ENDING UP WITH AN INVALID REFERENCE like omfg why.
 * ! So a custom Properties is needed to compute the proper file path.
 */
public class CustomItemProperties extends Item.Properties {

    private final String modelPath;
    public CustomItemProperties(String modelPath) {
        this.modelPath = modelPath;
    }

    @Override
    public Identifier effectiveModel() {
        return Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, modelPath);
    }
}
