package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.exceptions.ScriptException;

public interface Multipliable {
    Value multedBy(Value other) throws ScriptException;
}
