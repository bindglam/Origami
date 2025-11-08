package com.bindglam.origami.api.script.event;

import com.bindglam.origami.api.script.interpreter.value.primitive.AbstractFunction;
import com.bindglam.origami.api.script.interpreter.value.primitive.Function;
import com.bindglam.origami.api.script.interpreter.value.Value;

import java.util.*;
import java.util.concurrent.ExecutionException;

public final class EventRegistry implements AutoCloseable {
    private final Map<String, List<AbstractFunction>> functions = new HashMap<>();

    public void register(String type, Function function) {
        List<AbstractFunction> list = functions.computeIfAbsent(type, (t) -> new ArrayList<>());

        list.add(function);
    }

    public void callEvent(String type, List<Value<?>> args) {
        for(AbstractFunction function : functions.computeIfAbsent(type, (t) -> new ArrayList<>())) {
            try {
                function.executeAsync(args).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() {
        functions.clear();
    }
}
