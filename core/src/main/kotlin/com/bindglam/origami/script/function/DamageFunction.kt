package com.bindglam.origami.script.function

import com.bindglam.origami.api.OrigamiProvider
import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.exceptions.RuntimeException
import com.bindglam.origami.api.script.interpreter.value.bukkit.Entity
import com.bindglam.origami.api.script.interpreter.value.primitive.Number
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import org.bukkit.entity.LivingEntity

object DamageFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("DAMAGE")
            .args(Argument.builder().name("entity").build(), Argument.builder().name("amount").build(), Argument.builder().name("attacker").build())
            .body { context ->
                val entity = context.symbolTable().get("entity")
                val amount = context.symbolTable().get("amount")
                val attacker = context.symbolTable().get("attacker")

                if (entity !is Entity || amount !is Number || attacker !is Entity)
                    throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                val bukkitEntity = entity.bukkitEntity()
                if (bukkitEntity !is LivingEntity)
                    throw RuntimeException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, "You can damage only living entities", context.parent()!!)

                OrigamiProvider.origami().scheduler().task {
                    bukkitEntity.damage(amount.value(), attacker.bukkitEntity())
                }

                return@body null
            }
            .build()
    }
}