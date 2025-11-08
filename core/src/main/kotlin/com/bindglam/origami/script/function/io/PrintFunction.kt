package com.bindglam.origami.script.function.io

import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.script.function.BuiltInFunctionFactory

object PrintFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("PRINT")
            .args(Argument.builder().name("value").build())
            .body { context ->
                println(context.symbolTable().get("value").toString())

                return@body null
            }
            .build()
    }
}