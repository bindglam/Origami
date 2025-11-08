package com.bindglam.origami.api;

import com.bindglam.origami.api.manager.ScriptManager;
import com.bindglam.origami.api.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;

public interface Origami {
    ResultResult reload();

    @NotNull ScriptManager scriptManager();

    @NotNull Scheduler scheduler();

    enum ResultResult {
        SUCCESS,
        FAILURE
    }
}
