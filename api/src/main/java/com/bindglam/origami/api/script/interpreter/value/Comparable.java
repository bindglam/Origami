package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.exceptions.ScriptException;

public interface Comparable {
    Number compareLessThan(Value other) throws ScriptException;

    Number compareGreaterThan(Value other) throws ScriptException;

    Number compareLessThanEquals(Value other) throws ScriptException;

    Number compareGreaterThanEquals(Value other) throws ScriptException;
}
