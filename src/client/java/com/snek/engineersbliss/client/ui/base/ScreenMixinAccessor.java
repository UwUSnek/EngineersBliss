package com.snek.engineersbliss.client.ui.base;

import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;




public interface ScreenMixinAccessor {
    void eb$registerTextureCacheForClose(TextureCache cache);
}