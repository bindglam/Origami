package com.bindglam.origami.manager

import com.bindglam.origami.api.manager.ScriptManager
import com.bindglam.origami.api.script.Script
import com.bindglam.origami.api.script.exceptions.ScriptException
import com.bindglam.origami.api.script.interpreter.SymbolTable
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.script.function.*
import com.bindglam.origami.script.function.bukkit.DamageFunction
import com.bindglam.origami.script.function.bukkit.GetNearbyEntitiesFunction
import com.bindglam.origami.script.function.bukkit.KnockbackFunction
import com.bindglam.origami.script.function.bukkit.ParticleFunction
import com.bindglam.origami.script.function.bukkit.PlaySoundFunction
import com.bindglam.origami.script.function.bukkit.RegisterListenerFunction
import com.bindglam.origami.script.function.bukkit.SendMessageFunction
import com.bindglam.origami.script.function.io.DelayFunction
import com.bindglam.origami.script.function.io.PrintFunction
import com.bindglam.origami.script.function.math.CosFunction
import com.bindglam.origami.script.function.value.EntityToLocationFunction
import com.bindglam.origami.script.function.value.LocationFunction
import com.bindglam.origami.script.function.value.LocationToVector3Function
import com.bindglam.origami.script.function.math.NormalizeFunction
import com.bindglam.origami.script.function.math.SinFunction
import com.bindglam.origami.script.function.math.ToRadiansFunction
import com.bindglam.origami.script.function.value.LengthFunction
import com.bindglam.origami.script.function.value.Vector3Function
import java.io.File
import java.util.*
import java.util.logging.Logger

object ScriptManagerImpl : ScriptManager {
    private val scriptsFolder = File("plugins/Origami/scripts")

    private val logger = Logger.getLogger("Origami ScriptManager")

    private val loadedScripts = hashMapOf<String, Script>()

    private val builtInFunctions = arrayListOf<BuiltInFunction>()

    override fun start() {
        builtInFunctions.addAll(listOf(
            CosFunction, DamageFunction, DelayFunction, EntityToLocationFunction, GetNearbyEntitiesFunction,
            KnockbackFunction, LengthFunction, LocationFunction, LocationToVector3Function, NormalizeFunction,
            ParticleFunction, PlaySoundFunction, PrintFunction, RegisterListenerFunction, SendMessageFunction,
            SinFunction, ToRadiansFunction, Vector3Function
        ).map { it.create() })

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

    override fun reload() {
        end()

        start()
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

    override fun createSymbolTable(): SymbolTable = SymbolTable().apply {
        set("NULL", Number.NULL)
        set("TRUE", Number.TRUE)
        set("FALSE", Number.FALSE)

        builtInFunctions.forEach { set(it.name(), it) }
    }

    override fun getScript(id: String): Optional<Script> = Optional.ofNullable(loadedScripts[id])

    override fun printException(exception: ScriptException) {
        logger.info(exception.toString())
    }
}