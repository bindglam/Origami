package com.bindglam.origami.api.script.interpreter.value;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.Context;
import org.jetbrains.annotations.Nullable;

public record Entity(org.bukkit.entity.Entity bukkitEntity, @Nullable Position posStart, @Nullable Position posEnd, @Nullable Context context) implements Value {
    public Entity(org.bukkit.entity.Entity bukkitEntity) {
        this(bukkitEntity, null, null, null);
    }

    @Override
    public Value setPos(Position posStart, Position posEnd) {
        return new Entity(bukkitEntity, posStart, posEnd, context);
    }

    @Override
    public Value setContext(Context context) {
        return new Entity(bukkitEntity, posStart, posEnd, context);
    }

    @Override
    public Number compareEquals(Value other) {
        if(!(other instanceof Entity otherEntity)) return Number.FALSE;

        return bukkitEntity.getUniqueId().equals(otherEntity.bukkitEntity.getUniqueId()) ? Number.TRUE : Number.FALSE;
    }

    @Override
    public boolean isTrue() throws ScriptException {
        return bukkitEntity.isEmpty();
    }
}
