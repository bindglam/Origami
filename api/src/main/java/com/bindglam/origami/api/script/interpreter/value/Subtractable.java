package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.exceptions.ScriptException;

public interface Subtractable<T extends Value<T>> {
    T subbedTo(Value<?> other) throws ScriptException;
}
