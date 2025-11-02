package com.bindglam.origami.api.script.interpreter;

import com.bindglam.origami.api.Origami;
import com.bindglam.origami.api.script.interpreter.value.BuiltInFunction;
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
