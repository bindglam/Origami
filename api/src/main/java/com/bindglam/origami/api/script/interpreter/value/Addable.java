package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.exceptions.ScriptException;

public interface Addable<T extends Value<T>> {
    T addedTo(Value<?> other) throws ScriptException;
}
