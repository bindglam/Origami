package com.bindglam.origami.api.script.interpreter.value.math;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.Value;
import com.bindglam.origami.api.utils.math.LocationAdaptable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

public record Vector3(Vector3d vector, @NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) implements Value<Vector3> {
    public Vector3(Vector3d vector) {
        this(vector, Position.NONE, Position.NONE, Context.NONE);
    }

    @Override
    public Vector3 setInfo(@NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        return new Vector3(vector, posStart, posEnd, context);
    }

    @Override
    public com.bindglam.origami.api.script.interpreter.value.primitive.Number compareEquals(Value<?> other) {
        if(!(other instanceof Vector3 otherVec)) return com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;

        return vector.equals(otherVec.vector) ? com.bindglam.origami.api.script.interpreter.value.primitive.Number.TRUE : com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;
    }

    @Override
    public boolean isTrue() throws ScriptException {
        throw new RuntimeException(posStart, posEnd, "Unsupported operation", context);
    }

    @Override
    public String toString() {
        return vector.toString();
    }
}
