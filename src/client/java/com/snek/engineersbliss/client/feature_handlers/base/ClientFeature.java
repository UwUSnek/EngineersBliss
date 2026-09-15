package com.snek.engineersbliss.client.feature_handlers.base;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;








public class  ClientFeature<F extends __base_ServerFeature<?>> {
    private final F serverFeature;
    private final Supplier<UiTxt> nameSupplier;
    private final List<Object> descSuppliers;
    private UiTxt nameCache = null;
    private UiTxt descCache = null;


    public F getServerFeature() { return serverFeature; }
    public UiTxt getName() { if(nameCache == null) nameCache = calcName(); return (UiTxt)nameCache.copy(); }
    public UiTxt getDesc() { if(descCache == null) descCache = calcDesc(); return (UiTxt)descCache.copy(); }




    private UiTxt calcName() {
        return nameSupplier.get();
    }
    private void logInvalid(Object object) {
        EngineerSBliss.LOGGER.error(
            "Invalid object of type {} provided as ClientFeature description supplier. Affected feature: {}",
            object.getClass().getName(),
            getServerFeature().getId(),
            new Throwable()
        );
    }
    private UiTxt calcDesc() {
        UiTxt r = new UiTxt();
        for(final @NotNull Object e : descSuppliers) switch(e) {
            case @NotNull Supplier<?> s -> {
                final Object t = s.get();
                if(t instanceof UiTxt text) {
                    r.cat(text).cat("\n\n");
                }
                else logInvalid(e);
            }
            case @NotNull List<?> l -> {
                for(final @NotNull Object ee : l) {
                    if(ee instanceof @NotNull Supplier<?> s) {
                        final Object t = s.get();
                        if(t instanceof UiTxt text) {
                            r.cat(text).cat("\n");
                        }
                        else logInvalid(t);
                    }
                    else logInvalid(ee);
                }
                r.cat("\n");
            }
            default -> logInvalid(e);
        }
        return r;
    }




    /**
     * Creates a new ClientFeature.
     * @param serverFeature A UiTxt Supplier containing the display name of the feature.
     * @param nameSupplier A UiTxt Supplier containing the display name of the feature.
     * @param descSuppliers A list of Supplier<UiTxt> or List<Supplier<UiTxt>>, containing the description of the feature.
     *     Newlines are added between the elements of lists.
     *     Empty lines are added between lists and lone suppliers.
     */
    public ClientFeature(final F serverFeature, final Supplier<UiTxt> nameSupplier, final Object... descSuppliers) {
        this.serverFeature = serverFeature;
        this.nameSupplier = nameSupplier;
        this.descSuppliers = List.of(descSuppliers);
    }
}
