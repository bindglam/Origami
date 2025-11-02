package com.bindglam.origami.api.script.exceptions;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.utils.StringUtil;

public class ScriptException extends Exception {
    private final Position posStart;
    private final Position posEnd;
    private final String errorName;
    private final String details;

    public ScriptException(Position posStart, Position posEnd, String errorName, String details) {
        this.posStart = posStart;
        this.posEnd = posEnd;
        this.errorName = errorName;
        this.details = details;
    }

    public Position getPosStart() {
        return posStart;
    }

    public Position getPosEnd() {
        return posEnd;
    }

    public String getErrorName() {
        return errorName;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String getMessage() {
        return errorName + ": " + details + "\n" + "File " + posStart.getFn() + ", line " + (posStart.getLine() + 1) + "\n\n" + StringUtil.stringWithArrows(posStart.getText(), posStart, posEnd);
    }
}
