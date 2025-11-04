package com.bindglam.origami.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class OrigamiProvider {
    private static Origami instance;

    @ApiStatus.Internal
    public static void register(Origami instance) {
        if(OrigamiProvider.instance != null)
            throw new IllegalStateException("Already registered");

        OrigamiProvider.instance = instance;
    }

    public static @NotNull Origami origami() {
        if(instance == null)
            throw new IllegalStateException();

        return instance;
    }
}
