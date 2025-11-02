package com.bindglam.origami.api.script.exceptions;

import com.bindglam.origami.api.script.Position;

public class IllegalCharException extends ScriptException {
    public IllegalCharException(Position posStart, Position posEnd, String details) {
        super(posStart, posEnd, "Illegal Character", details);
    }
}
