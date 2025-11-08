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

    private val builtInFunctions = arrayListOf<BuiltInFunction>()

    override fun start() {
        fun registerInternalBuiltInFunctions() {
            registerBuiltInFunction(BuiltInFunction.builder()
                .name("LOCATION")
                .args(Argument.builder().name("world").build(), Argument.builder().name("x").build(), Argument.builder().name("y").build(), Argument.builder().name("z").build(), Argument.builder().name("yaw").build(), Argument.builder().name("pitch").build())
                .body { context ->
                    val world = context.symbolTable().get("world")
                    val x = context.symbolTable().get("x")
                    val y = context.symbolTable().get("y")
                    val z = context.symbolTable().get("z")
                    val yaw = context.symbolTable().get("yaw")
                    val pitch = context.symbolTable().get("pitch")

                    if (world !is com.bindglam.origami.api.script.interpreter.value.primitive.String || x !is Number || y !is Number || z !is Number
                        || yaw !is Number || pitch !is Number)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body Location(org.bukkit.Location(Bukkit.getWorld(world.value()), x.value(), y.value(), z.value(), yaw.value().toFloat(), pitch.value().toFloat()))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("VECTOR3")
                .args(Argument.builder().name("x").build(), Argument.builder().name("y").build(), Argument.builder().name("z").build())
                .body { context ->
                    val x = context.symbolTable().get("x")
                    val y = context.symbolTable().get("y")
                    val z = context.symbolTable().get("z")

                    if (x !is Number || y !is Number || z !is Number)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body Vector3(Vector3d(x.value(), y.value(), z.value()))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("LOCATION_TO_VECTOR3")
                .args(Argument.builder().name("location").build())
                .body { context ->
                    val location = context.symbolTable().get("location")

                    if (location !is LocationAdaptable)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body Vector3(Vector3d(location.location().x, location.location().y, location.location().z))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("ENTITY_TO_LOCATION")
                .args(Argument.builder().name("entity").build())
                .body { context ->
                    val entity = context.symbolTable().get("entity")

                    if (entity !is Entity)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body Location(entity.bukkitEntity().location)
                }
                .build()
            )


            registerBuiltInFunction(BuiltInFunction.builder()
                .name("PRINT")
                .args(Argument.builder().name("value").build())
                .body { context ->
                    println(context.symbolTable().get("value").toString())

                    return@body null
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("LEN")
                .args(Argument.builder().name("list").build())
                .body { context ->
                    val list = context.symbolTable().get("list")

                    if (list !is List)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body Number(list.list().size.toDouble())
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("DELAY")
                .args(Argument.builder().name("time").build())
                .body { context ->
                    val time = context.symbolTable().get("time")

                    if (time !is Number)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    Thread.sleep(time.value().toLong()*(1000L/20L))
                    return@body null
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("TO_RADIANS")
                .args(Argument.builder().name("angle").build())
                .body { context ->
                    val angle = context.symbolTable().get("angle")

                    if (angle !is Number)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body Number(Math.toRadians(angle.value()))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("COS")
                .args(Argument.builder().name("angle").build())
                .body { context ->
                    val angle = context.symbolTable().get("angle")

                    if (angle !is Number)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body Number(cos(angle.value()))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("SIN")
                .args(Argument.builder().name("angle").build())
                .body { context ->
                    val angle = context.symbolTable().get("angle")

                    if (angle !is Number)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body Number(sin(angle.value()))
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("NORMALIZE")
                .args(Argument.builder().name("vector").build())
                .body { context ->
                    val vector = context.symbolTable().get("vector")

                    if (vector !is Vector3)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body Vector3(vector.vector().normalize(Vector3d()))
                }
                .build()
            )


            registerBuiltInFunction(BuiltInFunction.builder()
                .name("REGISTER_LISTENER")
                .args(Argument.builder().name("type").build(), Argument.builder().name("func").build())
                .body { context ->
                    val type = context.symbolTable().get("type")
                    val func = context.symbolTable().get("func")

                    if (type !is com.bindglam.origami.api.script.interpreter.value.primitive.String || func !is Function)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    context.script().eventRegistry.register(type.value(), func)

                    return@body null
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("SEND_MESSAGE")
                .args(Argument.builder().name("entity").build(), Argument.builder().name("message").build())
                .body { context ->
                    val entity = context.symbolTable().get("entity")
                    val message = context.symbolTable().get("message")

                    if (entity !is Entity || message !is com.bindglam.origami.api.script.interpreter.value.primitive.String)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    entity.bukkitEntity().sendMessage(MiniMessage.miniMessage().deserialize(message.value()))

                    return@body null
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("DAMAGE")
                .args(Argument.builder().name("entity").build(), Argument.builder().name("amount").build(), Argument.builder().name("attacker").build())
                .body { context ->
                    val entity = context.symbolTable().get("entity")
                    val amount = context.symbolTable().get("amount")
                    val attacker = context.symbolTable().get("attacker")

                    if (entity !is Entity || amount !is Number || attacker !is Entity)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    val bukkitEntity = entity.bukkitEntity()
                    if (bukkitEntity !is LivingEntity)
                        throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "You can damage only living entities", context.parent()!!)

                    OrigamiProvider.origami().scheduler().task {
                        bukkitEntity.damage(amount.value(), attacker.bukkitEntity())
                    }

                    return@body null
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("KNOCKBACK")
                .args(Argument.builder().name("entity").build(), Argument.builder().name("vector").build())
                .body { context ->
                    val entity = context.symbolTable().get("entity")
                    val vector = context.symbolTable().get("vector")

                    if (entity !is Entity || vector !is Vector3)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    entity.bukkitEntity().velocity = Vector(vector.vector().x, vector.vector().y, vector.vector().z)

                    return@body null
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("PLAY_SOUND")
                .args(Argument.builder().name("location").build(), Argument.builder().name("key").build(), Argument.builder().name("volume").build(), Argument.builder().name("pitch").build())
                .body { context ->
                    val location = context.symbolTable().get("location")
                    val key = context.symbolTable().get("key")
                    val volume = context.symbolTable().get("volume")
                    val pitch = context.symbolTable().get("pitch")

                    if (location !is LocationAdaptable || key !is com.bindglam.origami.api.script.interpreter.value.primitive.String || volume !is Number || pitch !is Number)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    OrigamiProvider.origami().scheduler().task {
                        location.location().world.playSound(location.location(), key.value(), volume.value().toFloat(), pitch.value().toFloat())
                    }

                    return@body null
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("GET_NEAR_BY_ENTITIES")
                .args(Argument.builder().name("location").build(), Argument.builder().name("radius").build())
                .body { context ->
                    val location = context.symbolTable().get("location")
                    val radius = context.symbolTable().get("radius")

                    if (location !is LocationAdaptable || radius !is Number)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    return@body List(location.location().getNearbyLivingEntities(radius.value()).map { Entity(it) })
                }
                .build()
            )

            registerBuiltInFunction(BuiltInFunction.builder()
                .name("PARTICLE")
                .args(
                    Argument.builder().name("type").build(),
                    Argument.builder().name("location").build(),
                    Argument.builder().name("cnt").build(),
                    Argument.builder().name("offset").isOptional(true).build(),
                    Argument.builder().name("extra").isOptional(true).build())
                .body { context ->
                    val type = context.symbolTable().get("type")
                    val location = context.symbolTable().get("location")
                    val cnt = context.symbolTable().get("cnt")

                    val offset = context.symbolTable().get("offset")
                    val extra = context.symbolTable().get("extra")

                    if (type !is com.bindglam.origami.api.script.interpreter.value.primitive.String || location !is LocationAdaptable || cnt !is Number)
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)
                    if((offset != null && offset !is Vector3) || (extra != null && extra !is Number))
                        throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                    try {
                        val builder = Particle.valueOf(type.value()).builder()
                            .location(location.location())
                            .count(cnt.value().toInt())

                        if(offset != null)
                            builder.offset(offset.vector().x, offset.vector().y, offset.vector().z)
                        if(extra != null)
                            builder.extra(extra.value())

                        builder.spawn()
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