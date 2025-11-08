package com.bindglam.origami.api.script.interpreter.value.math;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.Value;
import com.bindglam.origami.api.utils.math.LocationAdaptable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.String;

public record Location(org.bukkit.Location bukkitLoc, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context) implements Value<Location>, LocationAdaptable {
    public Location(org.bukkit.Location bukkitLoc) {
        this(bukkitLoc, null, null, null);
    }

    @Override
    public Location setInfo(Position posStart, Position posEnd, Context context) {
        return new Location(bukkitLoc, posStart, posEnd, context);
    }

    @Override
    public com.bindglam.origami.api.script.interpreter.value.primitive.Number compareEquals(Value<?> other) {
        if(!(other instanceof Location otherLoc)) return com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;

        return bukkitLoc.equals(otherLoc.bukkitLoc) ? com.bindglam.origami.api.script.interpreter.value.primitive.Number.TRUE : com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;
    }

    @Override
    public boolean isTrue() throws ScriptException {
        throw new RuntimeException(posStart, posEnd, "Unsupported operation", context);
    }

    @Override
    public org.bukkit.@NotNull Location location() {
        return bukkitLoc;
    }

    @Override
    public String toString() {
        return bukkitLoc.toString();
    }
}
