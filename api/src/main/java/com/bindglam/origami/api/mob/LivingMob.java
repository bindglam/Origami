package com.bindglam.origami.api.mob;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface LivingMob {
    void remove();

    @NotNull Entity entity();
}
