package com.bindglam.origami.api.mob;

import com.bindglam.origami.api.script.RuntimeScript;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface LivingMob {
    void remove();

    @NotNull RuntimeScript script();

    @NotNull Entity entity();
}
