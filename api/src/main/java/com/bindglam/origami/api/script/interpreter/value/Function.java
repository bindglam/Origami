package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.Interpreter;
import com.bindglam.origami.api.script.interpreter.SymbolTable;
import com.bindglam.origami.api.script.node.Node;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public record Function(@Nullable java.lang.String name, Node body, List<java.lang.String> argNames, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context) implements Value {
    public Function(@Nullable java.lang.String name, Node body, List<java.lang.String> argNames) {
        this(name, body, argNames, null, null, null);
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
    public Value setPos(Position posStart, Position posEnd) {
        return new Function(name, body, argNames, posStart, posEnd, context);
    }

    @Override
    public Value setContext(Context context) {
        return new Function(name, body, argNames, posStart, posEnd, context);
    }

    @Override
    public Number compareEquals(Value other) {
        if(!(other instanceof Function otherFunc))
            return Number.FALSE;

        return (Objects.equals(name, otherFunc.name) ? Number.TRUE : Number.FALSE).setPos(posStart, posEnd).setContext(context);
    }

    @Override
    public boolean isTrue() throws ScriptException {
        throw new RuntimeException(posStart, posEnd, "Unsupported operation", context);
    }

    public Value execute(List<Value> args) throws ScriptException {
        Interpreter interpreter = new Interpreter();
        Context funcContext = new Context(name, context, posStart);
        funcContext.symbolTable(new SymbolTable(funcContext.parent().symbolTable()));

        if(args.size() > argNames.size())
            throw new RuntimeException(posStart, posEnd, (args.size() - argNames.size()) + "too many args passed into " + name, context);

        if(args.size() < argNames.size())
            throw new RuntimeException(posStart, posEnd, (argNames.size() - args.size()) + "too few args passed into " + name, context);

        for(int i = 0; i < args.size(); i++) {
            java.lang.String argName = argNames.get(i);
            Value argValue = args.get(i).setContext(funcContext);

            funcContext.symbolTable().set(argName, argValue);
        }

        return interpreter.visit(body, funcContext);
    }

    @Override
    public java.lang.String toString() {
        if(name != null)
            return "<function " + name + ">";
        return "<function <anonymous>>";
    }
}
