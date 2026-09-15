package com.snek.engineersbliss.utils.block_groups.base;

import net.minecraft.world.level.block.Block;




public class CopperBlockSet extends __base_BlockSet {
    public static class WeatheringStatesBlockSet extends __base_BlockSet {
        private final Block unaffected;
        private final Block exposed;
        private final Block weathered;
        private final Block oxidized;

        public Block unaffected() { return unaffected; }
        public Block    exposed() { return exposed;    }
        public Block  weathered() { return weathered;  }
        public Block   oxidized() { return oxidized;   }

        public WeatheringStatesBlockSet(final Block unaffected, final Block exposed, final Block weathered, final Block oxidized) {
            this.unaffected = register(unaffected);
            this.exposed    = register(exposed);
            this.weathered  = register(weathered);
            this.oxidized   = register(oxidized);
        }
    }

    private final WeatheringStatesBlockSet weathering;
    private final WeatheringStatesBlockSet waxed;

    public WeatheringStatesBlockSet weathering() { return weathering; }
    public WeatheringStatesBlockSet      waxed() { return waxed;      }


    public CopperBlockSet(
        final Block weatheringUnaffected,
        final Block weatheringExposed,
        final Block weatheringWeathered,
        final Block weatheringOxidized,
        final Block waxedUnaffected,
        final Block waxedExposed,
        final Block waxedWeathered,
        final Block waxedOxidized
    ) {
        this.weathering = new WeatheringStatesBlockSet(
            register(weatheringUnaffected),
            register(weatheringExposed),
            register(weatheringWeathered),
            register(weatheringOxidized)
        );
        this.waxed = new WeatheringStatesBlockSet(
            register(waxedUnaffected),
            register(waxedExposed),
            register(waxedWeathered),
            register(waxedOxidized)
        );
    }
}
