package com.bindglam.origami.manager

import com.bindglam.origami.api.manager.ScriptManager
import com.bindglam.origami.api.script.Script
import com.bindglam.origami.api.script.exceptions.RuntimeException
import com.bindglam.origami.api.script.exceptions.ScriptException
import com.bindglam.origami.api.script.interpreter.SymbolTable
import com.bindglam.origami.api.script.interpreter.value.primitive.BuiltInFunction
import com.bindglam.origami.api.script.interpreter.value.bukkit.Entity
import com.bindglam.origami.api.script.interpreter.value.primitive.Function
import com.bindglam.origami.api.script.interpreter.value.math.Location
import com.bindglam.origami.api.script.interpreter.value.math.Vector3
import com.bindglam.origami.api.utils.math.LocationAdaptable
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Particle
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

    private val builtInFunctions = arrayListOf<BuiltInFunction>()

    override fun start() {
        fun registerInternalBuiltInFunctions() {
            registerBuiltInFunction(BuiltInFunction.builder()
                .name("LOCATION")
                .args("world", "x", "y", "z", "yaw", "pitch")
                .body { context ->
                    val world = context.symbolTable().get("world")
                    val x = context.symbolTable().get("x")
                    val y = context.symbolTable().get("y")
                    val z = context.symbolTable().get("z")
                    val yaw = context.symbolTable().get("yaw")
                    val pitch = context.symbolTable().get("pitch")

                    if (world !is com.bindglam.origami.api.script.interpreter.value.primitive.String || x !is Number || y !is Number || z !is Number
                        || yaw !is Number || pitch !is Number)
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "Illegal arguments", context.parent()!!)

                    return@body Location(org.bukkit.Location(Bukkit.getWorld(world.value()), x.value(), y.value(), z.value(), yaw.value().toFloat(), pitch.value().toFloat()))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("VECTOR3")
                .args("x", "y", "z")
                .body { context ->
                    val x = context.symbolTable().get("x")
                    val y = context.symbolTable().get("y")
                    val z = context.symbolTable().get("z")

                    if (x !is Number || y !is Number || z !is Number)
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "Illegal arguments", context.parent()!!)

                    return@body Vector3(Vector3d(x.value(), y.value(), z.value()))
                }
                .build()
            )


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
                .name("TO_RADIANS")
                .args("angle")
                .body { context ->
                    val angle = context.symbolTable().get("angle")

                    if (angle !is Number)
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "Illegal arguments", context.parent()!!)

                    return@body Number(Math.toRadians(angle.value()))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("COS")
                .args("angle")
                .body { context ->
                    val angle = context.symbolTable().get("angle")

                    if (angle !is Number)
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "Illegal arguments", context.parent()!!)

                    return@body Number(cos(angle.value()))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("SIN")
                .args("angle")
                .body { context ->
                    val angle = context.symbolTable().get("angle")

                    if (angle !is Number)
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "Illegal arguments", context.parent()!!)

                    return@body Number(sin(angle.value()))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("REGISTER_LISTENER")
                .args("type", "func")
                .body { context ->
                    val type = context.symbolTable().get("type")
                    val func = context.symbolTable().get("func")

                    if (type !is com.bindglam.origami.api.script.interpreter.value.primitive.String || func !is Function)
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "Illegal arguments", context.parent()!!)

                    context.script().eventRegistry.register(type.value(), func)

                    return@body null
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("SEND_MESSAGE")
                .args("entity", "message")
                .body { context ->
                    val entity = context.symbolTable().get("entity")
                    val message = context.symbolTable().get("message")

                    if (entity !is Entity || message !is com.bindglam.origami.api.script.interpreter.value.primitive.String)
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "Illegal arguments", context.parent()!!)

                    entity.bukkitEntity().sendMessage(MiniMessage.miniMessage().deserialize(message.value()))

                    return@body null
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("PARTICLE")
                .args("type", "location", "offset", "cnt")
                .body { context ->
                    val type = context.symbolTable().get("type")
                    val location = context.symbolTable().get("location")
                    val offset = context.symbolTable().get("offset")
                    val cnt = context.symbolTable().get("cnt")

                    if (type !is com.bindglam.origami.api.script.interpreter.value.primitive.String || location !is LocationAdaptable || offset !is Vector3
                            || cnt !is Number)
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "Illegal arguments", context.parent()!!)

                    try {
                        Particle.valueOf(type.value()).builder()
                            .location(location.location())
                            .offset(offset.vector().x, offset.vector().y, offset.vector().z)
                            .count(cnt.value().toInt())
                            .spawn()
                    } catch (_: IllegalArgumentException) {
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "Unknown particle name", context.parent()!!)
                    }

                    return@body null
                }
                .build()
            )
        }

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