package com.bindglam.origami.api.events;

import com.bindglam.origami.api.script.interpreter.value.Value;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ScriptEventCallEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String id;
    private final List<Value<?>> args;

    public ScriptEventCallEvent(String id, List<Value<?>> args) {
        this.id = id;
        this.args = args;
    }

    public String getId() {
        return id;
    }

    public List<Value<?>> getArgs() {
        return args;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
