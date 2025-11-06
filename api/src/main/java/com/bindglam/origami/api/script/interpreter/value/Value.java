package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.primitive.Number;

public interface Value<T extends Value<T>> {
    Position posStart();

    Position posEnd();

    Context context();

    T setInfo(Position posStart, Position posEnd, Context context);

    Number compareEquals(Value<?> other);

    boolean isTrue() throws ScriptException;
}
