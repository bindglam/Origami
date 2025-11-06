package com.bindglam.origami.api.script.event;

import com.bindglam.origami.api.OrigamiProvider;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.value.primitive.Function;
import com.bindglam.origami.api.script.interpreter.value.Value;

import java.util.*;

public final class EventRegistry implements AutoCloseable {
    private final Map<String, List<Function>> functions = new HashMap<>();

    public void register(String type, Function function) {
        List<Function> list = functions.computeIfAbsent(type, (t) -> new ArrayList<>());

        list.add(function);
    }

    public void callEvent(String type, List<Value<?>> args) {
        for(Function function : functions.computeIfAbsent(type, (t) -> new ArrayList<>())) {
            try {
                function.execute(args);
            } catch (ScriptException e) {
                OrigamiProvider.origami().scriptManager().printException(e);
            }
        }
    }

    @Override
    public void close() {
        functions.clear();
    }
}
