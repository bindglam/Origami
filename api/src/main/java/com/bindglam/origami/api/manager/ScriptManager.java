package com.bindglam.origami.api.manager;

import com.bindglam.origami.api.script.Script;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.SymbolTable;
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ScriptManager extends ManagerBase, Reloadable {
    void compileAll();

    void registerBuiltInFunction(@NotNull BuiltInFunction function);

    @NotNull SymbolTable createSymbolTable();

    Optional<Script> getScript(@NotNull String id);

    void printException(@NotNull ScriptException exception);
}
