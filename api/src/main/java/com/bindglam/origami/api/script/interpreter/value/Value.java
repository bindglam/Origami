package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;

public interface Value extends Cloneable {
    Position posStart();

    Position posEnd();

    Value setPos(Position posStart, Position posEnd);

    Value setContext(Context context);

    Number compareEquals(Value other);

    boolean isTrue() throws ScriptException;
}
