package com.bindglam.origami.script.function.bukkit

import com.bindglam.origami.api.OrigamiProvider
import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.String
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.api.utils.math.LocationAdaptable
import com.bindglam.origami.script.function.BuiltInFunctionFactory

object PlaySoundFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("PLAY_SOUND")
            .args(Argument.builder().name("location").build(), Argument.builder().name("key").build(), Argument.builder().name("volume").build(), Argument.builder().name("pitch").build())
            .body { context ->
                val location = context.symbolTable().get("location")
                val key = context.symbolTable().get("key")
                val volume = context.symbolTable().get("volume")
                val pitch = context.symbolTable().get("pitch")

                if (location !is LocationAdaptable || key !is String || volume !is Number || pitch !is Number)
                    throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                OrigamiProvider.origami().scheduler().task {
                    location.location().world.playSound(location.location(), key.value(), volume.value().toFloat(), pitch.value().toFloat())
                }

                return@body null
            }
            .build()
    }
}