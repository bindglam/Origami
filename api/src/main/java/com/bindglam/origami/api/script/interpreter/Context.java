package com.bindglam.origami.api.script.interpreter;

import com.bindglam.origami.api.script.Position;
import org.jetbrains.annotations.Nullable;

public final class Context {
    private final String displayName;
    private final Context parent;
    private final Position parentEntryPosition;

    private SymbolTable symbolTable;

    public Context(String displayName, Context parent, Position parentEntryPosition) {
        this.displayName = displayName;
        this.parent = parent;
        this.parentEntryPosition = parentEntryPosition;
    }

    public String displayName() {
        return displayName;
    }

    public Context parent() {
        return parent;
    }

    public Position parentEntryPosition() {
        return parentEntryPosition;
    }

    public SymbolTable symbolTable() {
        return symbolTable;
    }

    public void symbolTable(SymbolTable table) {
        this.symbolTable = table;
    }
}
