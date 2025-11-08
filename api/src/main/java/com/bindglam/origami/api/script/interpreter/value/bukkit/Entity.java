package com.bindglam.origami.api.script.interpreter.value.bukkit;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.Value;
import com.bindglam.origami.api.utils.math.LocationAdaptable;
import org.jetbrains.annotations.NotNull;

import java.lang.String;

public record Entity(org.bukkit.entity.Entity bukkitEntity, @NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) implements Value<Entity>, LocationAdaptable {
    public Entity(org.bukkit.entity.Entity bukkitEntity) {
        this(bukkitEntity, Position.NONE, Position.NONE, Context.NONE);
    }

    @Override
    public Entity setInfo(@NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        return new Entity(bukkitEntity, posStart, posEnd, context);
    }

    @Override
    public com.bindglam.origami.api.script.interpreter.value.primitive.Number compareEquals(Value<?> other) {
        if(!(other instanceof Entity otherEntity)) return com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;

        return bukkitEntity.getUniqueId().equals(otherEntity.bukkitEntity.getUniqueId()) ? com.bindglam.origami.api.script.interpreter.value.primitive.Number.TRUE : com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;
    }

    @Override
    public boolean isTrue() {
        return bukkitEntity.isEmpty();
    }

    @Override
    public @NotNull org.bukkit.Location location() {
        return bukkitEntity.getLocation();
    }

    @Override
    public String toString() {
        return bukkitEntity.getName();
    }
}
