package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.Interpreter;
import com.bindglam.origami.api.script.node.Node;
import com.bindglam.origami.api.utils.ThrowingFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.String;
import java.util.Arrays;
import java.util.List;

public final class BuiltInFunction extends AbstractFunction {
    private final ThrowingFunction<Context, @Nullable Value, ScriptException> function;
    private final List<String> argNames;

    public BuiltInFunction(@Nullable String name, ThrowingFunction<Context, @Nullable Value, ScriptException> function, List<String> argNames, @Nullable Context context) {
        super(name, null, null, context);
        this.function = function;
        this.argNames = argNames;
    }

    public BuiltInFunction(@Nullable String name, ThrowingFunction<Context, @Nullable Value, ScriptException> function, List<String> argNames) {
        this(name, function, argNames, null);
    }

    @Override
    public Value setPos(Position posStart, Position posEnd) {
        return new BuiltInFunction(name(), function, argNames, context());
    }

    @Override
    public Value setContext(Context context) {
        return new BuiltInFunction(name(), function, argNames, context);
    }

    @Override
    public @Nullable Value execute(List<Value> args) throws ScriptException {
        Context executor = generateNewContext();

        checkArgs(argNames, args);
        populateArgs(argNames, args, executor);

        return function.apply(executor);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private List<String> args = List.of();
        private ThrowingFunction<Context, @Nullable Value, ScriptException> body;

        private Builder() {
        }

        public Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public Builder args(@NotNull List<String> args) {
            this.args = args;
            return this;
        }

        public Builder args(@NotNull String... args) {
            this.args = Arrays.stream(args).toList();
            return this;
        }

        public Builder body(@NotNull ThrowingFunction<Context, @Nullable Value, ScriptException> body) {
            this.body = body;
            return this;
        }

        public @NotNull BuiltInFunction build() {
            if(name == null)
                throw new IllegalStateException("name cannot be null");

            return new BuiltInFunction(name, body, args);
        }
    }
}
