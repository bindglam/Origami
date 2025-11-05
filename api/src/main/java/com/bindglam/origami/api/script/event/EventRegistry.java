package com.bindglam.origami.api.script.event;

import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.value.Function;
import com.bindglam.origami.api.script.interpreter.value.Value;

import java.util.*;

public final class EventRegistry implements AutoCloseable {
    private final Map<String, List<Function>> functions = new HashMap<>();

    public void register(String type, Function function) {
        List<Function> list = functions.computeIfAbsent(type, (t) -> new ArrayList<>());

        list.add(function);
    }

    public void callEvent(String type, List<Value> args) throws ScriptException {
        for(Function function : functions.computeIfAbsent(type, (t) -> new ArrayList<>())) {
            function.execute(args);
        }
    }

    @Override
    public void close() {
        functions.clear();
    }
}
