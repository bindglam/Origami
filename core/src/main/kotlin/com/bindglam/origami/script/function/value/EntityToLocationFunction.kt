package com.bindglam.origami.script.function.value

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.bukkit.Entity
import com.bindglam.origami.api.script.interpreter.value.math.Location
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.script.function.BuiltInFunctionFactory

object EntityToLocationFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("ENTITY_TO_LOCATION")
            .args(Argument.builder().name("entity").build())
            .body { context ->
                val entity = context.symbolTable().get("entity")

                if (entity !is Entity)
                    throw IllegalArgumentsException(
                        context.parentEntryPosition()!!,
                        context.parentEntryPosition()!!,
                        context.parent()!!
                    )

                return@body Location(entity.bukkitEntity().location)
            }
            .build()
    }
}