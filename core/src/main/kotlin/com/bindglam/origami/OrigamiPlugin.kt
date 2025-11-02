package com.bindglam.origami

import com.bindglam.origami.api.Origami
import com.bindglam.origami.api.script.interpreter.Context
import com.bindglam.origami.api.script.interpreter.Interpreter
import com.bindglam.origami.api.script.Lexer
import com.bindglam.origami.api.script.Parser
import com.bindglam.origami.api.script.exceptions.ScriptException
import com.bindglam.origami.api.script.interpreter.value.Number
import com.bindglam.origami.api.script.interpreter.SymbolTable
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandAPIPaperConfig
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.text.Component
import org.bukkit.plugin.java.JavaPlugin

class OrigamiPlugin : JavaPlugin(), Origami {
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIPaperConfig(this))

        CommandAPICommand("testscript")
            .withArguments(GreedyStringArgument("line"))
            .executes(CommandExecutor { sender, args ->
                val text = args["line"] as String

                val lexer = Lexer("<stdin>", text)

                try {
                    val tokens = lexer.makeTokens()
                    val parser = Parser(tokens)
                    val interpreter = Interpreter()
                    val context = Context("<program>", null, null)
                    context.symbolTable(SymbolTable(SymbolTable.GLOBAL_SYMBOL_TABLE))

                    val result = interpreter.visit(parser.parse(), context)

                    sender.sendMessage(Component.text(result.toString()))
                } catch (e: ScriptException) {
                    sender.sendMessage(Component.text(e.message!!))
                }
            })
            .register()
    }

    override fun onEnable() {
        CommandAPI.onEnable()
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }
}