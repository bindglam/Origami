package com.bindglam.origami.api.script.interpreter.value.primitive;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.Addable;
import com.bindglam.origami.api.script.interpreter.value.Value;
import org.jetbrains.annotations.Nullable;

public record String(java.lang.String value, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context) implements Value<String>, Addable<String> {
    public String(java.lang.String value) {
        this(value, null, null, null);
    }

    public String set(java.lang.String value) {
        return new String(value, posStart, posEnd, context);
    }

    @Override
    public String addedTo(Value<?> other) throws ScriptException {
        if(!(other instanceof String otherStr))
            throw new RuntimeException(posStart, posEnd, "Unsupported operation", context);

        return set(value + otherStr.value);
    }

    @Override
    public Number compareEquals(Value<?> other) {
        if(!(other instanceof String otherStr)) return Number.FALSE;

        return value.equals(otherStr.value) ? Number.TRUE : Number.FALSE;
    }

    @Override
    public String setInfo(Position posStart, Position posEnd, Context context) {
        return new String(value, posStart, posEnd, context);
    }

    @Override
    public boolean isTrue() {
        return !value.isEmpty();
    }

    @Override
    public java.lang.String toString() {
        return value;
    }
}
