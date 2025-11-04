package com.bindglam.origami

import com.bindglam.origami.api.Origami
import com.bindglam.origami.api.OrigamiProvider
import com.bindglam.origami.api.manager.ScriptManager
import com.bindglam.origami.api.script.interpreter.Context
import com.bindglam.origami.api.script.interpreter.Interpreter
import com.bindglam.origami.api.script.Lexer
import com.bindglam.origami.api.script.Parser
import com.bindglam.origami.api.script.exceptions.ScriptException
import com.bindglam.origami.api.script.interpreter.value.Number
import com.bindglam.origami.api.script.interpreter.SymbolTable
import com.bindglam.origami.manager.ScriptManagerImpl
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandAPIPaperConfig
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.text.Component
import org.bukkit.plugin.java.JavaPlugin

class OrigamiPlugin : JavaPlugin(), Origami {
    private val managers = listOf(
        ScriptManagerImpl
    )

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIPaperConfig(this))

        CommandAPICommand("testscript")
            .withArguments(GreedyStringArgument("line"))
            .executes(CommandExecutor { sender, args ->
                val text = args["line"] as String
            })
            .register()
    }

    override fun onEnable() {
        CommandAPI.onEnable()

        OrigamiProvider.register(this)

        managers.forEach { it.start() }
    }

    override fun onDisable() {
        CommandAPI.onDisable()

        managers.forEach { it.end() }
    }

    override fun scriptManager(): ScriptManager = ScriptManagerImpl
}