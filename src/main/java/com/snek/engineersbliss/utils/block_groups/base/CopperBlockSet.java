package com.snek.engineersbliss.utils.block_groups.base;

import java.util.Map;

import net.minecraft.world.level.block.Block;




public class CopperBlockSet extends __base_BlockSet {
    public static class WeatheringStatesBlockSet extends __base_BlockSet {
        public Map<String, Block> byOxidationState() { return byProperty; }

        private final Block unaffected;
        private final Block exposed;
        private final Block weathered;
        private final Block oxidized;

        public Block unaffected() { return unaffected; }
        public Block    exposed() { return exposed;    }
        public Block  weathered() { return weathered;  }
        public Block   oxidized() { return oxidized;   }

        public WeatheringStatesBlockSet(final Block unaffected, final Block exposed, final Block weathered, final Block oxidized) {
            this.unaffected = registerWithCustomData("unaffected", unaffected);
            this.exposed    = registerWithCustomData("exposed",    exposed   );
            this.weathered  = registerWithCustomData("weathered",  weathered );
            this.oxidized   = registerWithCustomData("oxidized",   oxidized  );
        }
    }



    public Map<String, Block> byWaxAndOxidationState() { return byProperty; }

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
            registerWithCustomData("weathering_unaffected", weatheringUnaffected),
            registerWithCustomData("weathering_exposed",    weatheringExposed   ),
            registerWithCustomData("weathering_weathered",  weatheringWeathered ),
            registerWithCustomData("weathering_oxidized",   weatheringOxidized  )
        );
        this.waxed = new WeatheringStatesBlockSet(
            registerWithCustomData("waxed_unaffected",      waxedUnaffected     ),
            registerWithCustomData("waxed_exposed",         waxedExposed        ),
            registerWithCustomData("waxed_weathered",       waxedWeathered      ),
            registerWithCustomData("waxed_oxidized",        waxedOxidized       )
        );
    }
}
