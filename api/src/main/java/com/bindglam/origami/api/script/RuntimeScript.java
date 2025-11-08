package com.bindglam.origami.api.script;

import com.bindglam.origami.api.OrigamiProvider;
import com.bindglam.origami.api.mob.LivingMob;
import com.bindglam.origami.api.script.event.EventRegistry;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.Interpreter;
import com.bindglam.origami.api.script.interpreter.SymbolTable;
import com.bindglam.origami.api.script.interpreter.value.bukkit.Entity;
import com.bindglam.origami.api.script.node.Node;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class RuntimeScript implements Runnable {
    private final Logger logger;
    private final LivingMob mob;
    private final Node code;

    private final EventRegistry eventRegistry = new EventRegistry();

    public RuntimeScript(Logger logger, LivingMob mob, Node code) {
        this.logger = logger;
        this.mob = mob;
        this.code = code;
    }

    @Override
    public void run() {
        try {
            Interpreter interpreter = new Interpreter();

            Context context = new Context("<program>", null, null, this);
            SymbolTable symbolTable = OrigamiProvider.origami().scriptManager().createSymbolTable();
            symbolTable.set("SELF", new Entity(mob.entity()));
            context.symbolTable(symbolTable);

            interpreter.visit(code, context);
        } catch (ScriptException e) {
            logger.severe(e.toString());
        }
    }

    public void close() {
        eventRegistry.close();
    }

    public @NotNull EventRegistry getEventRegistry() {
        return eventRegistry;
    }
}
