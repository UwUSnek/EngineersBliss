package com.snek.engineersbliss.utils.block_groups.base;

import java.util.Map;

import net.minecraft.world.level.block.Block;




public class ColoredBlockSet extends __base_BlockSet {
    public Map<String, Block> byColor() { return byProperty; }


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


    public ColoredBlockSet(
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
        this.white     = registerWithCustomData("white",      white    );
        this.orange    = registerWithCustomData("orange",     orange   );
        this.magenta   = registerWithCustomData("magenta",    magenta  );
        this.lightBlue = registerWithCustomData("light_blue", lightBlue);
        this.yellow    = registerWithCustomData("yellow",     yellow   );
        this.lime      = registerWithCustomData("lime",       lime     );
        this.pink      = registerWithCustomData("pink",       pink     );
        this.gray      = registerWithCustomData("gray",       gray     );
        this.lightGray = registerWithCustomData("light_gray", lightGray);
        this.cyan      = registerWithCustomData("cyan",       cyan     );
        this.purple    = registerWithCustomData("purple",     purple   );
        this.blue      = registerWithCustomData("blue",       blue     );
        this.brown     = registerWithCustomData("brown",      brown    );
        this.green     = registerWithCustomData("green",      green    );
        this.red       = registerWithCustomData("red",        red      );
        this.black     = registerWithCustomData("black",      black    );
    }
}
