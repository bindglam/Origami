package com.bindglam.origami.api.script.interpreter;

import com.bindglam.origami.api.OrigamiProvider;
import com.bindglam.origami.api.script.event.EventType;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.interpreter.value.BuiltInFunction;
import com.bindglam.origami.api.script.interpreter.value.Function;
import com.bindglam.origami.api.script.interpreter.value.Value;
import com.bindglam.origami.api.script.interpreter.value.Number;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SymbolTable {
    public static final SymbolTable GLOBAL_SYMBOL_TABLE = new SymbolTable();

    static {
        GLOBAL_SYMBOL_TABLE.set("NULL", Number.NULL);
        GLOBAL_SYMBOL_TABLE.set("TRUE", Number.TRUE);
        GLOBAL_SYMBOL_TABLE.set("FALSE", Number.FALSE);

        GLOBAL_SYMBOL_TABLE.set("PRINT", new BuiltInFunction("PRINT", (context) -> {
            Value value = Objects.requireNonNull(context.symbolTable().get("value"));

            System.out.println(value);

            return null;
        }, List.of("value")));

        GLOBAL_SYMBOL_TABLE.set("REGISTER_LISTENER", new BuiltInFunction("REGISTER_LISTENER", (context) -> {
            Value typeValue = Objects.requireNonNull(context.symbolTable().get("type"));
            Value funcValue = Objects.requireNonNull(context.symbolTable().get("func"));

            if(!(typeValue instanceof com.bindglam.origami.api.script.interpreter.value.String typeStr)
                    || !(funcValue instanceof Function func))
                throw new RuntimeException(context.parentEntryPosition(), context.parentEntryPosition(), "Illegal arguments", context.parent());

            EventType type;
            try {
                type = EventType.valueOf(typeStr.value());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(context.parentEntryPosition(), context.parentEntryPosition(), "Unknown event type", context.parent());
            }

            context.script().getEventRegistry().register(type, func);

            return null;
        }, List.of("type", "func")));
    }

    private final Map<String, Value> symbols = new HashMap<>();

    private SymbolTable parent;

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    public SymbolTable() {
        this(null);
    }

    public @Nullable Value get(String name) {
        Value value = symbols.get(name);
        if(value == null && parent != null)
            return parent.get(name);
        return value;
    }

    public void set(String name, Value value) {
        symbols.put(name, value);
    }

    public void remove(String name) {
        symbols.remove(name);
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }
}
