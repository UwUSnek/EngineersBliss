package com.snek.engineersbliss.utils.block_groups.base;

import net.minecraft.world.level.block.Block;




public class ColouredBlockSet extends __base_BlockSet {
    private final Block white;
    private final Block orange;
    private final Block magenta;
    private final Block lightBlue;
    private final Block yellow;
    private final Block lime;
    private final Block pink;
    private final Block gray;
    private final Block lightGray;
    private final Block cyan;
    private final Block purple;
    private final Block blue;
    private final Block brown;
    private final Block green;
    private final Block red;
    private final Block black;


    public Block     white(){ return white;     }
    public Block    orange(){ return orange;    }
    public Block   magenta(){ return magenta;   }
    public Block lightBlue(){ return lightBlue; }
    public Block    yellow(){ return yellow;    }
    public Block      lime(){ return lime;      }
    public Block      pink(){ return pink;      }
    public Block      gray(){ return gray;      }
    public Block lightGray(){ return lightGray; }
    public Block      cyan(){ return cyan;      }
    public Block    purple(){ return purple;    }
    public Block      blue(){ return blue;      }
    public Block     brown(){ return brown;     }
    public Block     green(){ return green;     }
    public Block       red(){ return red;       }
    public Block     black(){ return black;     }


    public ColouredBlockSet(
        final Block white,
        final Block orange,
        final Block magenta,
        final Block lightBlue,
        final Block yellow,
        final Block lime,
        final Block pink,
        final Block gray,
        final Block lightGray,
        final Block cyan,
        final Block purple,
        final Block blue,
        final Block brown,
        final Block green,
        final Block red,
        final Block black
    ) {
        this.white     = register(white);
        this.orange    = register(orange);
        this.magenta   = register(magenta);
        this.lightBlue = register(lightBlue);
        this.yellow    = register(yellow);
        this.lime      = register(lime);
        this.pink      = register(pink);
        this.gray      = register(gray);
        this.lightGray = register(lightGray);
        this.cyan      = register(cyan);
        this.purple    = register(purple);
        this.blue      = register(blue);
        this.brown     = register(brown);
        this.green     = register(green);
        this.red       = register(red);
        this.black     = register(black);
    }
}
