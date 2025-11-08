package com.bindglam.origami.script.function

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import kotlin.math.cos

object CosFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("COS")
            .args(Argument.builder().name("angle").build())
            .body { context ->
                val angle = context.symbolTable().get("angle")

                if (angle !is Number)
                    throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                return@body Number(cos(angle.value()))
            }
            .build()
    }
}