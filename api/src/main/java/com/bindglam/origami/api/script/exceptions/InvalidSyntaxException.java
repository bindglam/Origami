package com.bindglam.origami.api.script.exceptions;

import com.bindglam.origami.api.script.Position;

public class InvalidSyntaxException extends ScriptException {
    public InvalidSyntaxException(Position posStart, Position posEnd, String details) {
        super(posStart, posEnd, "Invalid Syntax", details);
    }

    public InvalidSyntaxException(Position posStart, Position posEnd) {
        super(posStart, posEnd, "Invalid Syntax", "");
    }
}
