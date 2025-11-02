package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.Interpreter;
import com.bindglam.origami.api.script.node.Node;
import org.jetbrains.annotations.Nullable;

import java.lang.String;
import java.util.List;

public final class Function extends AbstractFunction {
    private final Node body;
    private final List<String> argNames;

    public Function(@Nullable java.lang.String name, Node body, List<java.lang.String> argNames, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context) {
        super(name, posStart, posEnd, context);
        this.body = body;
        this.argNames = argNames;
    }

    public Function(@Nullable java.lang.String name, Node body, List<java.lang.String> argNames) {
        this(name, body, argNames, null, null, null);
    }

    @Override
    public Value setPos(Position posStart, Position posEnd) {
        return new Function(name(), body, argNames, posStart, posEnd, context());
    }

    @Override
    public Value setContext(Context context) {
        return new Function(name(), body, argNames, posStart(), posEnd(), context);
    }

    @Override
    public @Nullable Value execute(List<Value> args) throws ScriptException {
        Interpreter interpreter = new Interpreter();
        Context executor = generateNewContext();

        checkArgs(argNames, args);
        populateArgs(argNames, args, executor);

        return interpreter.visit(body, executor);
    }
}
