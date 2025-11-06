package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.exceptions.ScriptException;

public interface Divisible<T extends Value<T>> {
    T divedBy(Value<?> other) throws ScriptException;
}
