package com.snek.engineersbliss.client.feature_handlers.base;

import java.util.function.Supplier;

import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;




public class __base_ClientFeatureSet<S extends __base_ServerFeatureSet> {
    private final S serverSet;
    private final Supplier<UiTxt> nameSupplier;
    private UiTxt nameCache = null;

    //! Helper method to avoid writing Supplier<UiTxt> everywhere
    protected static Supplier<UiTxt> s(Supplier<UiTxt> supplier) { return supplier; }


    public S getServerSet() { return serverSet; }
    public UiTxt getName() { if(nameCache == null) nameCache = calcName(); return (UiTxt)nameCache.copy(); }


    private UiTxt calcName() {
        return nameSupplier.get();
    }


    protected __base_ClientFeatureSet(final S serverSet, final Supplier<UiTxt> nameSupplier) {
        this.serverSet = serverSet;
        this.nameSupplier = nameSupplier;
    }
}
