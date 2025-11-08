package com.bindglam.origami.script.function.value

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.math.Vector3
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.script.function.BuiltInFunctionFactory
import org.joml.Vector3d

object Vector3Function : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
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
    }
}