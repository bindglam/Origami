package com.bindglam.origami.api.script.interpreter.value.primitive;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.Value;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record List(java.util.List<Value<?>> list, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context) implements Value<List> {
    public List(java.util.List<Value<?>> list) {
        this(list, null, null, null);
    }

    @Override
    public List setInfo(Position posStart, Position posEnd, Context context) {
        return new List(list, posStart, posEnd, context);
    }

    @Override
    public Number compareEquals(Value<?> other) {
        if(!(other instanceof List otherList))
            return Number.FALSE;

        return Objects.equals(list, otherList.list) ? Number.TRUE : Number.FALSE;
    }

    @Override
    public boolean isTrue() throws ScriptException {
        throw new RuntimeException(posStart, posEnd, "Unsupported operation", context);
    }

    @Override
    public java.lang.String toString() {
        return "[" + java.lang.String.join(", ", list.stream().map((v) -> v != null ? v.toString() : "undefined").toList()) + "]";
    }
}
