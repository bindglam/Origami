package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.value.primitive.Number;

public interface Comparable {
    com.bindglam.origami.api.script.interpreter.value.primitive.Number compareLessThan(Value<?> other) throws ScriptException;

    com.bindglam.origami.api.script.interpreter.value.primitive.Number compareGreaterThan(Value<?> other) throws ScriptException;

    com.bindglam.origami.api.script.interpreter.value.primitive.Number compareLessThanEquals(Value<?> other) throws ScriptException;

    Number compareGreaterThanEquals(Value<?> other) throws ScriptException;
}
