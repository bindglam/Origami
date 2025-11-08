package com.bindglam.origami.script.function

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.bukkit.Entity
import com.bindglam.origami.api.script.interpreter.value.primitive.List
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.api.utils.math.LocationAdaptable

object GetNearbyEntitiesFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
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
    }
}