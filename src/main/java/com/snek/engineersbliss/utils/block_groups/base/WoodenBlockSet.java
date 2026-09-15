package com.snek.engineersbliss.utils.block_groups.base;

import java.util.Map;

import net.minecraft.world.level.block.Block;




public class WoodenBlockSet extends __base_BlockSet {
    public Map<String, Block> byWoodType() { return byProperty; }


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
        this.oak      = registerWithCustomData("oak",       oak     );
        this.spruce   = registerWithCustomData("spruce",    spruce  );
        this.birch    = registerWithCustomData("birch",     birch   );
        this.jungle   = registerWithCustomData("jungle",    jungle  );
        this.acacia   = registerWithCustomData("acacia",    acacia  );
        this.darkOak  = registerWithCustomData("dark_oak",  darkOak );
        this.mangrove = registerWithCustomData("mangrove",  mangrove);
        this.cherry   = registerWithCustomData("cherry",    cherry  );
        this.paleOak  = registerWithCustomData("pale_oak",  paleOak );
        this.bamboo   = registerWithCustomData("bamboo",    bamboo  );
        this.crimson  = registerWithCustomData("crimson",   crimson );
        this.warped   = registerWithCustomData("warped",    warped  );
    }
}

//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+
//TODO ADD POPLAR WOOD 26.3+