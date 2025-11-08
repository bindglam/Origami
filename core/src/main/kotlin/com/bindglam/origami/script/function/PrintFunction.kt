package com.bindglam.origami.script.function

import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction

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