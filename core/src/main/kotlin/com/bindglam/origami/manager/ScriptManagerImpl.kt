package com.bindglam.origami.manager

import com.bindglam.origami.api.OrigamiProvider
import com.bindglam.origami.api.manager.ScriptManager
import com.bindglam.origami.api.script.Script
import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.exceptions.RuntimeException
import com.bindglam.origami.api.script.exceptions.ScriptException
import com.bindglam.origami.api.script.interpreter.SymbolTable
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.api.script.interpreter.value.bukkit.Entity
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Function
import com.bindglam.origami.api.script.interpreter.value.math.Location
import com.bindglam.origami.api.script.interpreter.value.math.Vector3
import com.bindglam.origami.api.script.interpreter.value.primitive.List
import com.bindglam.origami.api.utils.math.LocationAdaptable
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.script.function.CosFunction
import com.bindglam.origami.script.function.DamageFunction
import com.bindglam.origami.script.function.DelayFunction
import com.bindglam.origami.script.function.EntityToLocationFunction
import com.bindglam.origami.script.function.GetNearbyEntitiesFunction
import com.bindglam.origami.script.function.KnockbackFunction
import com.bindglam.origami.script.function.LengthFunction
import com.bindglam.origami.script.function.LocationFunction
import com.bindglam.origami.script.function.LocationToVector3Function
import com.bindglam.origami.script.function.NormalizeFunction
import com.bindglam.origami.script.function.ParticleFunction
import com.bindglam.origami.script.function.PlaySoundFunction
import com.bindglam.origami.script.function.PrintFunction
import com.bindglam.origami.script.function.RegisterListenerFunction
import com.bindglam.origami.script.function.SendMessageFunction
import com.bindglam.origami.script.function.SinFunction
import com.bindglam.origami.script.function.ToRadiansFunction
import com.bindglam.origami.script.function.Vector3Function
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import org.joml.Vector3d
import java.io.File
import java.util.*
import java.util.logging.Logger
import kotlin.math.cos
import kotlin.math.sin

object ScriptManagerImpl : ScriptManager {
    private val scriptsFolder = File("plugins/Origami/scripts")

    private val logger = Logger.getLogger("Origami ScriptManager")

    private val loadedScripts = hashMapOf<String, Script>()

    private val builtInFunctions = ArrayList<BuiltInFunction>(listOf(
        CosFunction, DamageFunction, DelayFunction, EntityToLocationFunction, GetNearbyEntitiesFunction,
        KnockbackFunction, LengthFunction, LocationFunction, LocationToVector3Function, NormalizeFunction,
        ParticleFunction, PlaySoundFunction, PrintFunction, RegisterListenerFunction, SendMessageFunction,
        SinFunction, ToRadiansFunction, Vector3Function
    ).map { it.create() })

    override fun start() {
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