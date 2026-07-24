package com.snek.engineersbliss.client.feature_handlers.base;

import java.util.function.Supplier;

import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;




public class __base_ClientFeatureSet<S extends __base_ServerFeatureSet> {
    private final Supplier<UiTxt> nameSupplier;
    private final S serverSet;


    public UiTxt calcName() { return nameSupplier.get(); }
    public S getServerSet() { return serverSet; }


    protected __base_ClientFeatureSet(final S serverSet, final Supplier<UiTxt> nameSupplier) {
        this.serverSet = serverSet;
        this.nameSupplier = nameSupplier;
    }
}
