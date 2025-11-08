package com.bindglam.origami.script.function

import com.bindglam.origami.api.script.interpreter.value.primitive.function.BuiltInFunction

interface BuiltInFunctionFactory {
    fun create(): BuiltInFunction
}