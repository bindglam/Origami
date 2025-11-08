package com.bindglam.origami.script.function

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.math.Vector3
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import org.joml.Vector3d

object NormalizeFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("NORMALIZE")
            .args(Argument.builder().name("vector").build())
            .body { context ->
                val vector = context.symbolTable().get("vector")

                if (vector !is Vector3)
                    throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                return@body Vector3(vector.vector().normalize(Vector3d()))
            }
            .build()
    }
}