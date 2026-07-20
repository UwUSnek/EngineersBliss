package com.snek.engineersbliss.client.feature_handlers.base;

import java.util.function.Supplier;

import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.utils.Txt;








public class  ClientFeature<F extends __base_ServerFeature<?>> {
    private final F serverFeature;
    private final Supplier<Txt> nameSupplier;
    private final Supplier<Txt> descSupplier;


    public F getServerFeature() { return serverFeature; }
    public Txt calcName() { return nameSupplier.get(); }
    public Txt calcDesc() { return descSupplier.get(); }


    public ClientFeature(final F serverFeature, final Supplier<Txt> nameSupplier, final Supplier<Txt> descSupplier) {
        this.serverFeature = serverFeature;
        this.nameSupplier = nameSupplier;
        this.descSupplier = descSupplier;
    }
}
