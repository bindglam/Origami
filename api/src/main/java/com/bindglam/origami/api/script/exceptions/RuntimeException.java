package com.bindglam.origami.api.script.exceptions;

import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.utils.StringUtil;

import java.util.Objects;

public class RuntimeException extends ScriptException {
    private final Context context;

    public RuntimeException(Position posStart, Position posEnd, String details, Context context) {
        super(posStart, posEnd, "Runtime Exception", details);
        this.context = context;
    }

    @Override
    public String getMessage() {
        String result = generateTraceback();

        result += getErrorName() + ": " + getDetails() + "\n\n" + StringUtil.stringWithArrows(getPosStart().getText(), getPosStart(), getPosEnd());

        return result;
    }

    private String generateTraceback() {
        StringBuilder result = new StringBuilder();
        Position pos = getPosStart();
        Context ctx = context;

        while(ctx != null) {
            result.insert(0, "  File " + Objects.requireNonNull(pos).getFn() + ", line " + (pos.getLine() + 1) + ", in " + ctx.displayName() + "\n");
            pos = ctx.parentEntryPosition();
            ctx = ctx.parent();
        }

        return "Traceback (most recent call last):\n" + result;
    }
}
