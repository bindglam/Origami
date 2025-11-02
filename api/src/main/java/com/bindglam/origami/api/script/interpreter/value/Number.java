package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import org.jetbrains.annotations.Nullable;

public record Number(double value, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context)
        implements Value, Addable, Subtractable, Multipliable, Divisible, Comparable {
    public static final Number NULL = new Number(0);
    public static final Number FALSE = new Number(0);
    public static final Number TRUE = new Number(1);

    public Number(double value) {
        this(value, null, null, null);
    }

    @Override
    public Number setPos(Position posStart, Position posEnd) {
        return new Number(value, posStart, posEnd, context);
    }

    @Override
    public Number setContext(Context context) {
        return new Number(value, posStart, posEnd, context);
    }

    public Number set(double value) {
        return new Number(value, posStart, posEnd, context);
    }

    @Override
    public Value addedTo(Value other) throws RuntimeException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart, other.posEnd(), "Unsupported operation", context);

        return set(value + otherNum.value);
    }

    @Override
    public Value subbedTo(Value other) throws RuntimeException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart, other.posEnd(), "Unsupported operation", context);

        return set(value - otherNum.value);
    }

    @Override
    public Value multedBy(Value other) throws RuntimeException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart, other.posEnd(), "Unsupported operation", context);

        return set(value * otherNum.value);
    }

    @Override
    public Value divedBy(Value other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart, other.posEnd(), "Unsupported operation", context);

        if(Double.compare(otherNum.value, 0.0) == 0)
            throw new RuntimeException(otherNum.posStart, otherNum.posEnd, "Division by zero", context);
        return set(value / otherNum.value);
    }

    public Number powedBy(Number other) {
        return set(Math.pow(value, other.value));
    }

    @Override
    public Number compareEquals(Value other) {
        if(!(other instanceof Number otherNum))
            return set(0.0);

        return set(Double.compare(value, otherNum.value) == 0 ? 1.0 : 0.0);
    }

    @Override
    public Number compareLessThan(Value other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart, other.posEnd(), "Unsupported operation", context);

        return set(Double.compare(value, otherNum.value) < 0 ? 1 : 0);
    }

    @Override
    public Number compareGreaterThan(Value other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart, other.posEnd(), "Unsupported operation", context);

        return set(Double.compare(value, otherNum.value) > 0 ? 1 : 0);
    }

    @Override
    public Number compareLessThanEquals(Value other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart, other.posEnd(), "Unsupported operation", context);

        return set(Double.compare(value, otherNum.value) <= 0 ? 1 : 0);
    }

    @Override
    public Number compareGreaterThanEquals(Value other) throws ScriptException {
        if(!(other instanceof Number otherNum))
            throw new RuntimeException(posStart, other.posEnd(), "Unsupported operation", context);

        return set(Double.compare(value, otherNum.value) >= 0 ? 1 : 0);
    }

    public Number not() {
        return set(isTrue() ? 0.0 : 1.0);
    }

    @Override
    public boolean isTrue() {
        return Double.compare(value, 0.0) != 0;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.String.valueOf(value);
    }
}
