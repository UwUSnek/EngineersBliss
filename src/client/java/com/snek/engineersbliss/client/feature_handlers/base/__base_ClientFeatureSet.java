package com.snek.engineersbliss.client.feature_handlers.base;

import java.util.function.Supplier;

import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;
import com.snek.engineersbliss.utils.Txt;




public class __base_ClientFeatureSet<S extends __base_ServerFeatureSet> {
    private final Supplier<Txt> nameSupplier;
    private final S serverSet;


    public Txt calcName() { return nameSupplier.get(); }
    public S getServerSet() { return serverSet; }


    protected __base_ClientFeatureSet(final S serverSet, final Supplier<Txt> nameSupplier) {
        this.serverSet = serverSet;
        this.nameSupplier = nameSupplier;
    }
}
