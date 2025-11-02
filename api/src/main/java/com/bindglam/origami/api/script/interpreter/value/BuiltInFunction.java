package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.Interpreter;
import com.bindglam.origami.api.script.node.Node;
import com.bindglam.origami.api.utils.ThrowingFunction;
import org.jetbrains.annotations.Nullable;

import java.lang.String;
import java.util.List;

public final class BuiltInFunction extends AbstractFunction {
    private final ThrowingFunction<Context, @Nullable Value, ScriptException> function;
    private final List<String> argNames;

    public BuiltInFunction(@Nullable String name, ThrowingFunction<Context, @Nullable Value, ScriptException> function, List<String> argNames, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context) {
        super(name, posStart, posEnd, context);
        this.function = function;
        this.argNames = argNames;
    }

    public BuiltInFunction(@Nullable String name, ThrowingFunction<Context, @Nullable Value, ScriptException> function, List<String> argNames) {
        this(name, function, argNames, null, null, null);
    }

    @Override
    public Value setPos(Position posStart, Position posEnd) {
        return new BuiltInFunction(name(), function, argNames, posStart, posEnd, context());
    }

    @Override
    public Value setContext(Context context) {
        return new BuiltInFunction(name(), function, argNames, posStart(), posEnd(), context);
    }

    @Override
    public @Nullable Value execute(List<Value> args) throws ScriptException {
        Context executor = generateNewContext();

        checkArgs(argNames, args);
        populateArgs(argNames, args, executor);

        return function.apply(executor);
    }
}
