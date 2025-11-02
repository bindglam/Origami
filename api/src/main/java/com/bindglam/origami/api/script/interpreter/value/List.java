package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record List(java.util.List<Value> list, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context) implements Value {
    public List(java.util.List<Value> list) {
        this(list, null, null, null);
    }

    @Override
    public List setPos(Position posStart, Position posEnd) {
        return new List(list, posStart, posEnd, context);
    }

    @Override
    public List setContext(Context context) {
        return new List(list, posStart, posEnd, context);
    }

    @Override
    public Number compareEquals(Value other) {
        if(!(other instanceof List otherList))
            return Number.FALSE.setPos(posStart, posEnd).setContext(context);

        return (Objects.equals(list, otherList.list) ? Number.TRUE : Number.FALSE).setPos(posStart, posEnd).setContext(context);
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
