package com.snek.engineersbliss.client.feature_handlers.base;

import java.util.function.Supplier;

import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;








public class  ClientFeature<F extends __base_ServerFeature<?>> {
    private final F serverFeature;
    private final Supplier<UiTxt> nameSupplier;
    private final Supplier<UiTxt> descSupplier;


    public F getServerFeature() { return serverFeature; }
    public UiTxt calcName() { return nameSupplier.get(); }
    public UiTxt calcDesc() { return descSupplier.get(); }


    public ClientFeature(final F serverFeature, final Supplier<UiTxt> nameSupplier, final Supplier<UiTxt> descSupplier) {
        this.serverFeature = serverFeature;
        this.nameSupplier = nameSupplier;
        this.descSupplier = descSupplier;
    }
}
