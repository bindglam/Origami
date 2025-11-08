package com.bindglam.origami.api.script.interpreter.value.math;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.exceptions.UnsupportedOperationException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public record Vector3(Vector3d vector, @NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context)
        implements Value<Vector3>, Addable<Vector3>, Subtractable<Vector3>, Multipliable<Vector3>, Divisible<Vector3> {
    public Vector3(Vector3d vector) {
        this(vector, Position.NONE, Position.NONE, Context.NONE);
    }

    @Override
    public Vector3 setInfo(@NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        return new Vector3(vector, posStart, posEnd, context);
    }

    @Override
    public Vector3 addedTo(Value<?> other) throws ScriptException {
        if(!(other instanceof Vector3 otherVec))
            throw new UnsupportedOperationException(posStart, posEnd, context);
        return new Vector3(vector.add(otherVec.vector, new Vector3d()));
    }

    @Override
    public Vector3 subbedTo(Value<?> other) throws ScriptException {
        if(!(other instanceof Vector3 otherVec))
            throw new UnsupportedOperationException(posStart, posEnd, context);
        return new Vector3(vector.sub(otherVec.vector, new Vector3d()));
    }

    @Override
    public Vector3 multedBy(Value<?> other) throws ScriptException {
        if(other instanceof Vector3 otherVec)
            return new Vector3(vector.mul(otherVec.vector, new Vector3d()));
        else if(other instanceof com.bindglam.origami.api.script.interpreter.value.primitive.Number otherNum)
            return new Vector3(vector.mul(otherNum.value(), new Vector3d()));
        else
            throw new UnsupportedOperationException(posStart, posEnd, context);
    }

    @Override
    public Vector3 divedBy(Value<?> other) throws ScriptException {
        if(other instanceof Vector3 otherVec)
            return new Vector3(vector.div(otherVec.vector, new Vector3d()));
        else if(other instanceof com.bindglam.origami.api.script.interpreter.value.primitive.Number otherNum)
            return new Vector3(vector.div(otherNum.value(), new Vector3d()));
        else
            throw new UnsupportedOperationException(posStart, posEnd, context);
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
