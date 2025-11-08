package com.bindglam.origami.script.function

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.math.Location
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import org.bukkit.Bukkit

object LocationFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
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
    }
}