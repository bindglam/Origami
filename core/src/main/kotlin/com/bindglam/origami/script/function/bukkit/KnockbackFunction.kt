package com.bindglam.origami.script.function.bukkit

import com.bindglam.origami.api.script.exceptions.IllegalArgumentsException
import com.bindglam.origami.api.script.interpreter.value.bukkit.Entity
import com.bindglam.origami.api.script.interpreter.value.math.Vector3
import com.bindglam.origami.api.script.interpreter.value.primitive.function.Argument
import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction
import com.bindglam.origami.script.function.BuiltInFunctionFactory
import org.bukkit.util.Vector

object KnockbackFunction : BuiltInFunctionFactory {
    override fun create(): BuiltInFunction {
        return BuiltInFunction.builder()
            .name("KNOCKBACK")
            .args(Argument.builder().name("entity").build(), Argument.builder().name("vector").build())
            .body { context ->
                val entity = context.symbolTable().get("entity")
                val vector = context.symbolTable().get("vector")

                if (entity !is Entity || vector !is Vector3)
                    throw IllegalArgumentsException(context.parentEntryPosition()!!, context.parentEntryPosition()!!, context.parent()!!)

                entity.bukkitEntity().velocity = Vector(vector.vector().x, vector.vector().y, vector.vector().z)

                return@body null
            }
            .build()
    }
}