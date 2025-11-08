package com.bindglam.origami.script.function.value

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.primitive.List
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.script.function.BuiltInFunctionFactory

object LengthFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("LEN")
            .args(Argument.builder().name("list").build())
            .body { context ->
                val list = context.symbolTable().get("list")

                if (list !is List)
                    throw IllegalArgumentsException(
                        context.parentEntryPosition()!!,
                        context.parentEntryPosition()!!,
                        context.parent()!!
                    )

                return@body Number(list.list().size.toDouble())
            }
            .build()
    }
}