package com.bindglam.origami.api.script.event;

import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.value.Function;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class EventRegistry implements AutoCloseable {
    private final Map<EventType, List<Function>> functions = new EnumMap<>(EventType.class);

    public void register(EventType type, Function function) {
        List<Function> list = functions.computeIfAbsent(type, (t) -> new ArrayList<>());

        list.add(function);
    }

    public void callEvent(EventType type) throws ScriptException {
        for(Function function : functions.computeIfAbsent(type, (t) -> new ArrayList<>())) {
            function.execute(List.of());
        }
    }

    @Override
    public void close() {
        functions.clear();
    }
}
