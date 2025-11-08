package com.bindglam.origami.api.script.interpreter.value.primitive.function;

import com.bindglam.origami.api.OrigamiProvider;
import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.exceptions.UnsupportedOperationException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.SymbolTable;
import com.bindglam.origami.api.script.interpreter.value.Value;
import com.bindglam.origami.api.utils.math.IntRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.String;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public abstract class AbstractFunction implements Value<AbstractFunction> {
    private final String name;
    private final Position posStart;
    private final Position posEnd;
    private final Context context;

    public AbstractFunction(@Nullable String name, @NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        this.name = name;
        this.posStart = posStart;
        this.posEnd = posEnd;
        this.context = context;
    }

    public abstract @Nullable Value<?> execute(List<Value<?>> args) throws ScriptException;

    public CompletableFuture<@Nullable Value<?>> executeAsync(List<Value<?>> args) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return execute(args);
            } catch (ScriptException e) {
                OrigamiProvider.origami().scriptManager().printException(e);
            }
            return null;
        });
    }

    protected Context generateNewContext() {
        Context newContext = new Context(name, context, posStart, context.script());

        newContext.symbolTable(newContext.parent() != null ? new SymbolTable(newContext.parent().symbolTable()) : new SymbolTable());

        return newContext;
    }

    protected void checkArgs(List<Argument> args, List<Value<?>> argValues, Context funcContext) throws ScriptException {
        boolean isOptionalPart = false;
        for(Argument arg : args) {
            if(!arg.isOptional() && isOptionalPart)
                throw new RuntimeException(posStart, posEnd, "Optional argument should be at last", funcContext);

            if(arg.isOptional())
                isOptionalPart = true;
        }

        long requiredArgs = args.stream().filter((arg) -> !arg.isOptional()).count();
        IntRange range = new IntRange((int) requiredArgs, args.size());

        if(argValues.size() > range.max())
            throw new RuntimeException(posStart, posEnd, (argValues.size() - range.max()) + " too many args passed into " + name, funcContext);

        if(argValues.size() < range.min())
            throw new RuntimeException(posStart, posEnd, (range.min() - argValues.size()) + " too few args passed into " + name, funcContext);
    }

    protected void populateArgs(List<Argument> args, List<Value<?>> argValues, Context executor) {
        for(int i = 0; i < argValues.size(); i++) {
            Argument arg = args.get(i);
            Value<?> argValue = argValues.get(i);
            argValue = argValue.setInfo(argValue.posStart(), argValue.posEnd(), executor);

            executor.symbolTable().set(arg.name(), argValue);
        }
    }

    public String name() {
        return name;
    }

    @Override
    public @NotNull Position posStart() {
        return posStart;
    }

    @Override
    public @NotNull Position posEnd() {
        return posEnd;
    }

    @Override
    public @NotNull Context context() {
        return context;
    }

    @Override
    public com.bindglam.origami.api.script.interpreter.value.primitive.Number compareEquals(Value<?> other) {
        if(!(other instanceof AbstractFunction otherFunc))
            return com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;

        return Objects.equals(name, otherFunc.name) ? com.bindglam.origami.api.script.interpreter.value.primitive.Number.TRUE : com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;
    }

    @Override
    public boolean isTrue() throws ScriptException {
        throw new UnsupportedOperationException(posStart, posEnd, context);
    }

    @Override
    public java.lang.String toString() {
        if(name != null)
            return "<function " + name + ">";
        return "<function <anonymous>>";
    }
}
