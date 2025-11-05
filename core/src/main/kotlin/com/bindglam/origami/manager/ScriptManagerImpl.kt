package com.bindglam.origami.manager

import com.bindglam.origami.api.manager.ScriptManager
import com.bindglam.origami.api.script.Script
import com.bindglam.origami.api.script.exceptions.RuntimeException
import com.bindglam.origami.api.script.interpreter.SymbolTable
import com.bindglam.origami.api.script.interpreter.value.BuiltInFunction
import com.bindglam.origami.api.script.interpreter.value.Function
import com.bindglam.origami.api.script.interpreter.value.Number
import java.io.File
import java.util.*
import java.util.logging.Logger

object ScriptManagerImpl : ScriptManager {
    private val scriptsFolder = File("plugins/Origami/scripts")

    private val logger = Logger.getLogger("Origami ScriptManager")

    private val loadedScripts = hashMapOf<String, Script>()

    private val builtInFunctions = arrayListOf<BuiltInFunction>()

    override fun start() {
        registerInternalBuiltInFunctions()

        if(!scriptsFolder.exists())
            scriptsFolder.mkdirs()

        scriptsFolder.walkBottomUp().forEach { file ->
            if(file.isDirectory) return@forEach

            loadedScripts[file.nameWithoutExtension] = Script(logger, file)
        }

        compileAll()
    }

    override fun end() {
        loadedScripts.clear()

        builtInFunctions.clear()
    }

    override fun compileAll() {
        logger.info("Compiling scripts...")

        loadedScripts.forEach { id, script -> script.compile() }

        logger.info("Successfully compiled all scripts!")
    }

    override fun registerBuiltInFunction(function: BuiltInFunction) {
        if(builtInFunctions.any { func -> func.name() == function.name() })
            throw IllegalStateException("Already registered")

        builtInFunctions.add(function)
    }

    private fun registerInternalBuiltInFunctions() {
        registerBuiltInFunction(BuiltInFunction.builder()
            .name("PRINT")
            .args("value")
            .body { context ->
                println(context.symbolTable().get("value").toString())

                return@body null
            }
            .build()
        )

        registerBuiltInFunction(BuiltInFunction.builder()
            .name("REGISTER_LISTENER")
            .args("type", "func")
            .body { context ->
                val type = context.symbolTable().get("type")
                val func = context.symbolTable().get("func")

                if (type !is com.bindglam.origami.api.script.interpreter.value.String || func !is Function)
                    throw RuntimeException(context.parentEntryPosition(), context.parentEntryPosition(), "Illegal arguments", context.parent())

                context.script().eventRegistry.register(type.value, func)

                return@body null
            }
            .build()
        )
    }

    override fun createSymbolTable(): SymbolTable = SymbolTable().apply {
        set("NULL", Number.NULL)
        set("TRUE", Number.TRUE)
        set("FALSE", Number.FALSE)

        builtInFunctions.forEach { set(it.name(), it) }
    }

    override fun getScript(id: String): Optional<Script> = Optional.ofNullable(loadedScripts[id])
}