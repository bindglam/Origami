package com.bindglam.origami.api.script.interpreter.value.primitive.function;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.Value;
import com.bindglam.origami.api.utils.ThrowingFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.String;
import java.util.Arrays;
import java.util.List;

public final class BuiltInFunction extends AbstractFunction {
    private final ThrowingFunction<Context, @Nullable Value<?>, ScriptException> function;
    private final List<Argument> args;

    public BuiltInFunction(@Nullable String name, ThrowingFunction<Context, @Nullable Value<?>, ScriptException> function, List<Argument> args,
                           @NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        super(name, posStart, posEnd, context);
        this.function = function;
        this.args = args;
    }

    public BuiltInFunction(@Nullable String name, ThrowingFunction<Context, @Nullable Value<?>, ScriptException> function, List<Argument> args) {
        this(name, function, args, Position.NONE, Position.NONE, Context.NONE);
    }

    @Override
    public BuiltInFunction setInfo(@NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        return new BuiltInFunction(name(), function, args, posStart, posEnd, context);
    }

    @Override
    public @Nullable Value<?> execute(List<Value<?>> argValues) throws ScriptException {
        Context executor = generateNewContext();

        checkArgs(args, argValues, executor);
        populateArgs(args, argValues, executor);

        return function.apply(executor);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private List<Argument> args = List.of();
        private ThrowingFunction<Context, @Nullable Value<?>, ScriptException> body;

        private Builder() {
        }

        public Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public Builder args(@NotNull List<Argument> args) {
            this.args = args;
            return this;
        }

        public Builder args(@NotNull Argument... args) {
            this.args = Arrays.stream(args).toList();
            return this;
        }

        public Builder body(@NotNull ThrowingFunction<Context, @Nullable Value<?>, ScriptException> body) {
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
