package com.bindglam.origami.script.function

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.bukkit.Entity
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import net.kyori.adventure.text.minimessage.MiniMessage

object SendMessageFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("SEND_MESSAGE")
            .args(Argument.builder().name("entity").build(), Argument.builder().name("message").build())
            .body { context ->
                val entity = context.symbolTable().get("entity")
                val message = context.symbolTable().get("message")

                if (entity !is Entity || message !is com.bindglam.origami.api.script.interpreter.value.primitive.String)
                    throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                entity.bukkitEntity().sendMessage(MiniMessage.miniMessage().deserialize(message.value()))

                return@body null
            }
            .build()
    }
}