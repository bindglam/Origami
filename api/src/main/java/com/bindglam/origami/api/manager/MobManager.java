package com.bindglam.origami.api.manager;

import com.bindglam.origami.api.mob.LivingMob;
import com.bindglam.origami.api.mob.OrigamiMob;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface MobManager extends ManagerBase {
    Optional<OrigamiMob> getMob(@NotNull String id);

    Optional<LivingMob> getLivingMob(@NotNull Entity entity);

    boolean isLivingMob(@NotNull Entity entity);
}
