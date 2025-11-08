package com.bindglam.origami.api.script.exceptions;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.interpreter.Context;
import org.jetbrains.annotations.NotNull;

public class IllegalArgumentsException extends RuntimeException {
    public IllegalArgumentsException(@NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        super(posStart, posEnd, "Illegal arguments", context);
    }
}
