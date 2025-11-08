package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.primitive.Number;
import org.jetbrains.annotations.NotNull;

public interface Value<T extends Value<T>> {
    @NotNull Position posStart();

    @NotNull Position posEnd();

    @NotNull Context context();

    T setInfo(@NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context);

    Number compareEquals(Value<?> other);

    boolean isTrue() throws ScriptException;
}
