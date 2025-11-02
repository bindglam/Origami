package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.exceptions.ScriptException;

public interface Divisible {
    Value divedBy(Value other) throws ScriptException;
}
