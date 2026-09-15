package com.snek.engineersbliss.utils.block_groups.base;

import net.minecraft.world.level.block.Block;




public class WoodenBlockSet extends __base_BlockSet {
    private Block oak;
    private Block spruce;
    private Block birch;
    private Block jungle;
    private Block acacia;
    private Block darkOak;
    private Block mangrove;
    private Block cherry;
    private Block paleOak;
    private Block bamboo;
    private Block crimson;
    private Block warped;


    public Block      oak() { return oak;      }
    public Block   spruce() { return spruce;   }
    public Block    birch() { return birch;    }
    public Block   jungle() { return jungle;   }
    public Block   acacia() { return acacia;   }
    public Block  darkOak() { return darkOak;  }
    public Block mangrove() { return mangrove; }
    public Block   cherry() { return cherry;   }
    public Block  paleOak() { return paleOak;  }
    public Block   bamboo() { return bamboo;   }
    public Block  crimson() { return crimson;  }
    public Block   warped() { return warped;   }


    public WoodenBlockSet(
        final Block oak,
        final Block spruce,
        final Block birch,
        final Block jungle,
        final Block acacia,
        final Block darkOak,
        final Block mangrove,
        final Block cherry,
        final Block paleOak,
        final Block bamboo,
        final Block crimson,
        final Block warped
    ) {
        this.oak      = register(oak);
        this.spruce   = register(spruce);
        this.birch    = register(birch);
        this.jungle   = register(jungle);
        this.acacia   = register(acacia);
        this.darkOak  = register(darkOak);
        this.mangrove = register(mangrove);
        this.cherry   = register(cherry);
        this.paleOak  = register(paleOak);
        this.bamboo   = register(bamboo);
        this.crimson  = register(crimson);
        this.warped   = register(warped);
    }
}

//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+