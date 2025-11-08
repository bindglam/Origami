package com.bindglam.origami.api.script.exceptions;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.interpreter.Context;
import org.jetbrains.annotations.NotNull;

public class DivisionByZeroException extends RuntimeException {
    public DivisionByZeroException(@NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        super(posStart, posEnd, "Division by zero", context);
    }
}
