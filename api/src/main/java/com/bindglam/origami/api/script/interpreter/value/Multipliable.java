package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.exceptions.ScriptException;

public interface Multipliable<T extends Value<T>> {
    T multedBy(Value<?> other) throws ScriptException;
}
