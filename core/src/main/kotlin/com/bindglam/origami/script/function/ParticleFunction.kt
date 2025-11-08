package com.bindglam.origami.script.function

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.exceptions.RuntimeException
import com.bindglam.origami.api.script.interpreter.value.math.Vector3
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.api.utils.math.LocationAdaptable
import org.bukkit.Particle

object ParticleFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
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
    }
}