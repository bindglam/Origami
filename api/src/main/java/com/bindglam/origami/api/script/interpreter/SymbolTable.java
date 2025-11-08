package com.bindglam.origami.api.script.interpreter;

import com.bindglam.origami.api.script.interpreter.value.Value;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class SymbolTable {
    private final Map<String, Value<?>> symbols = new HashMap<>();

    private SymbolTable parent;

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    public SymbolTable() {
        this(null);
    }

    public @Nullable Value<?> get(String name) {
        Value<?> value = symbols.get(name);
        if(value == null && parent != null)
            return parent.get(name);
        return value;
    }

    public void set(String name, Value<?> value) {
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
