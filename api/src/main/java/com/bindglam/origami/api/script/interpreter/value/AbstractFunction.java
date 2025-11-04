package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.SymbolTable;
import org.jetbrains.annotations.Nullable;

import java.lang.String;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFunction implements Value {
    private final String name;
    private final Position posStart;
    private final Position posEnd;
    private final Context context;

    public AbstractFunction(@Nullable String name, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context) {
        this.name = name;
        this.posStart = posStart;
        this.posEnd = posEnd;
        this.context = context;
    }

    public abstract @Nullable Value execute(List<Value> args) throws ScriptException;

    protected Context generateNewContext() {
        if(context == null)
            throw new IllegalStateException("context cannot be null");

        Context newContext = new Context(name, context, posStart, context.script());

        newContext.symbolTable(newContext.parent() != null ? new SymbolTable(newContext.parent().symbolTable()) : new SymbolTable());

        return newContext;
    }

    protected void checkArgs(List<String> argNames, List<Value> args) throws ScriptException {
        if(args.size() > argNames.size())
            throw new RuntimeException(posStart, posEnd, (args.size() - argNames.size()) + " too many args passed into " + name, context);

        if(args.size() < argNames.size())
            throw new RuntimeException(posStart, posEnd, (argNames.size() - args.size()) + " too few args passed into " + name, context);
    }

    protected void populateArgs(List<String> argNames, List<Value> args, Context executor) {
        for(int i = 0; i < args.size(); i++) {
            String argName = argNames.get(i);
            Value argValue = args.get(i).setContext(executor);

            executor.symbolTable().set(argName, argValue);
        }
    }

    public String name() {
        return name;
    }

    @Override
    public Position posStart() {
        return posStart;
    }

    @Override
    public Position posEnd() {
        return posEnd;
    }

    @Override
    public Context context() {
        return context;
    }

    @Override
    public Number compareEquals(Value other) {
        if(!(other instanceof AbstractFunction otherFunc))
            return Number.FALSE;

        return Objects.equals(name, otherFunc.name) ? Number.TRUE : Number.FALSE;
    }

    @Override
    public boolean isTrue() throws ScriptException {
        throw new RuntimeException(posStart, posEnd, "Unsupported operation", context);
    }

    @Override
    public java.lang.String toString() {
        if(name != null)
            return "<function " + name + ">";
        return "<function <anonymous>>";
    }
}
