package com.bindglam.origami.script.function.bukkit

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.primitive.String
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Function
import com.bindglam.origami.script.function.BuiltInFunctionFactory

object RegisterListenerFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("REGISTER_LISTENER")
            .args(Argument.builder().name("type").build(), Argument.builder().name("func").build())
            .body { context ->
                val type = context.symbolTable().get("type")
                val func = context.symbolTable().get("func")

                if (type !is String || func !is Function)
                    throw IllegalArgumentsException(
                        context.parentEntryPosition()!!,
                        context.parentEntryPosition()!!,
                        context.parent()!!
                    )

                context.script().eventRegistry.register(type.value(), func)

                return@body null
            }
            .build()
    }
}