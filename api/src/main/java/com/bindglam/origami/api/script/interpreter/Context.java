package com.bindglam.origami.api.script.interpreter;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.RuntimeScript;
import org.jetbrains.annotations.Nullable;

public final class Context {
    private final String displayName;
    private final Context parent;
    private final Position parentEntryPosition;
    private final RuntimeScript script;

    private SymbolTable symbolTable;

    public Context(String displayName, Context parent, Position parentEntryPosition, RuntimeScript script) {
        this.displayName = displayName;
        this.parent = parent;
        this.parentEntryPosition = parentEntryPosition;
        this.script = script;
    }

    public String displayName() {
        return displayName;
    }

    public @Nullable Context parent() {
        return parent;
    }

    public @Nullable Position parentEntryPosition() {
        return parentEntryPosition;
    }

    public SymbolTable symbolTable() {
        return symbolTable;
    }

    public void symbolTable(SymbolTable table) {
        this.symbolTable = table;
    }

    public RuntimeScript script() {
        return script;
    }
}
