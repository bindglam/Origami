package com.bindglam.origami.api.mob;

import com.bindglam.origami.api.mob.properties.MobProperties;
import com.bindglam.origami.api.script.Script;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface OrigamiMob {
    MobProperties properties();

    Script script();

    @NotNull LivingMob spawn(Location location);
}
