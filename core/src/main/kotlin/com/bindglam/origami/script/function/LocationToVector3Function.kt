package com.bindglam.origami.script.function

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.math.Vector3
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.api.utils.math.LocationAdaptable
import org.joml.Vector3d

object LocationToVector3Function : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("LOCATION_TO_VECTOR3")
            .args(Argument.builder().name("location").build())
            .body { context ->
                val location = context.symbolTable().get("location")

                if (location !is LocationAdaptable)
                    throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                return@body Vector3(Vector3d(location.location().x, location.location().y, location.location().z))
            }
            .build()
    }
}