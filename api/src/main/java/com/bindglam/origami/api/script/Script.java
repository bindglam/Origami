package com.bindglam.origami.api.script;

import com.bindglam.origami.api.mob.LivingMob;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.node.Node;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class Script {
    private final Logger logger;
    private final File file;

    private Node code = null;

    public Script(Logger logger, File file) {
        this.logger = logger;
        this.file = file;
    }

    public void compile() {
        String text;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            text = reader.lines().collect(Collectors.joining("\n"));
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Lexer lexer = new Lexer(file.getName(), text);
            Parser parser = new Parser(lexer.makeTokens());

            code = parser.parse();
        } catch (ScriptException e) {
            logger.severe(e.toString());
        }
    }

    public @NotNull RuntimeScript execute(LivingMob mob) {
        if(code == null)
            compile();

        RuntimeScript script = new RuntimeScript(logger, mob, code);
        script.run();
        return script;
    }

    public CompletableFuture<@NotNull RuntimeScript> executeAsync(LivingMob mob) {
        return CompletableFuture.supplyAsync(() -> execute(mob));
    }
}
