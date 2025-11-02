package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.exceptions.ScriptException;

public interface Subtractable {
    Value subbedTo(Value other) throws ScriptException;
}
