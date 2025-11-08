package com.bindglam.origami.api.script.interpreter.value.primitive;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.*;
import com.bindglam.origami.api.script.interpreter.value.Comparable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record Number(double value, @NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context)
        implements Value<Number>, Addable<Number>, Subtractable<Number>, Multipliable<Number>, Divisible<Number>, Comparable {
    public static final Number NULL = new Number(0);
    public static final Number FALSE = new Number(0);
    public static final Number TRUE = new Number(1);

    public Number(double value) {
        this(value, Position.NONE, Position.NONE, Context.NONE);
    }

    public Number set(double value) {
        return new Number(value, posStart, posEnd, context);
    }

    @Override
    public Number addedTo(Value<?> other) throws RuntimeException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(Objects.requireNonNull(posStart()), other.posEnd(), "Unsupported operation", Objects.requireNonNull(context()));

        return set(value + otherNum.value);
    }

    @Override
    public Number subbedTo(Value<?> other) throws RuntimeException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(Objects.requireNonNull(posStart()), other.posEnd(), "Unsupported operation", Objects.requireNonNull(context()));

        return set(value - otherNum.value);
    }

    @Override
    public Number multedBy(Value<?> other) throws RuntimeException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(Objects.requireNonNull(posStart()), other.posEnd(), "Unsupported operation", Objects.requireNonNull(context()));

        return set(value * otherNum.value);
    }

    @Override
    public Number divedBy(Value<?> other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart(), other.posEnd(), "Unsupported operation", context());

        if(Double.compare(otherNum.value, 0.0) == 0)
            throw new RuntimeException(otherNum.posStart(), otherNum.posEnd(), "Division by zero", context());
        return set(value / otherNum.value);
    }

    public Number powedBy(Number other) {
        return set(Math.pow(value, other.value));
    }

    @Override
    public Number compareEquals(Value<?> other) {
        if(!(other instanceof Number otherNum))
            return set(0.0);

        return set(Double.compare(value, otherNum.value) == 0 ? 1.0 : 0.0);
    }

    @Override
    public Number compareLessThan(Value<?> other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart(), other.posEnd(), "Unsupported operation", context());

        return set(Double.compare(value, otherNum.value) < 0 ? 1 : 0);
    }

    @Override
    public Number compareGreaterThan(Value<?> other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart(), other.posEnd(), "Unsupported operation", context());

        return set(Double.compare(value, otherNum.value) > 0 ? 1 : 0);
    }

    @Override
    public Number compareLessThanEquals(Value<?> other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart(), other.posEnd(), "Unsupported operation", context());

        return set(Double.compare(value, otherNum.value) <= 0 ? 1 : 0);
    }

    @Override
    public Number compareGreaterThanEquals(Value<?> other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart(), other.posEnd(), "Unsupported operation", context());

        return set(Double.compare(value, otherNum.value) >= 0 ? 1 : 0);
    }

    @Override
    public Number setInfo(@NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        return new Number(value, posStart, posEnd, context);
    }

    @Override
    public boolean isTrue() {
        return Double.compare(value, 0.0) != 0;
    }

    public Number not() {
        return isTrue() ? FALSE : TRUE;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.String.valueOf(value);
    }
}
