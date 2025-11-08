package com.bindglam.origami.api.scheduler;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface Scheduler {
    long TICK_MILLIS = 1000L/20L;

    void task(@NotNull Runnable action);

    void taskLater(@NotNull Runnable action, long tick);

    void taskTimer(@NotNull Runnable action, long delay, long interval);

    void taskRegion(@NotNull Runnable action, @NotNull Location location);

    void taskRegionLater(@NotNull Runnable action, @NotNull Location location, long tick);

    void taskRegionTimer(@NotNull Runnable action, @NotNull Location location, long delay, long interval);

    void taskAsync(@NotNull Runnable action);

    void taskAsyncLater(@NotNull Runnable action, long tick);

    void taskAsyncTimer(@NotNull Runnable action, long delay, long interval);
}
