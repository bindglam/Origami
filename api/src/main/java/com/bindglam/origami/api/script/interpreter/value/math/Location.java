package com.bindglam.origami.api.script.interpreter.value.math;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.exceptions.DivisionByZeroException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.exceptions.UnsupportedOperationException;
import com.bindglam.origami.api.script.interpreter.Context;
import com.bindglam.origami.api.script.interpreter.value.*;
import com.bindglam.origami.api.utils.math.LocationAdaptable;
import org.jetbrains.annotations.NotNull;

import java.lang.String;

public record Location(org.bukkit.Location bukkitLoc, @NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context)
        implements Value<Location>, Addable<Location>, Subtractable<Location>, Multipliable<Location>, Divisible<Location>, LocationAdaptable {
    public Location(org.bukkit.Location bukkitLoc) {
        this(bukkitLoc, Position.NONE, Position.NONE, Context.NONE);
    }

    @Override
    public Location setInfo(@NotNull Position posStart, @NotNull Position posEnd, @NotNull Context context) {
        return new Location(bukkitLoc, posStart, posEnd, context);
    }

    @Override
    public Location addedTo(Value<?> other) throws ScriptException {
        if(other instanceof LocationAdaptable otherLoc)
            return new Location(bukkitLoc.clone().add(otherLoc.location()));
        else if(other instanceof Vector3 otherVec)
            return new Location(bukkitLoc.clone().add(otherVec.vector().x, otherVec.vector().y, otherVec.vector().z));
        else
            throw new UnsupportedOperationException(posStart, posEnd, context);
    }

    @Override
    public Location subbedTo(Value<?> other) throws ScriptException {
        if(other instanceof LocationAdaptable otherLoc)
            return new Location(bukkitLoc.clone().subtract(otherLoc.location()));
        else if(other instanceof Vector3 otherVec)
            return new Location(bukkitLoc.clone().subtract(otherVec.vector().x, otherVec.vector().y, otherVec.vector().z));
        else
            throw new UnsupportedOperationException(posStart, posEnd, context);
    }

    @Override
    public Location multedBy(Value<?> other) throws ScriptException {
        return switch (other) {
            case LocationAdaptable otherLoc ->
                    new Location(new org.bukkit.Location(bukkitLoc.getWorld(), bukkitLoc.x() * otherLoc.location().x(), bukkitLoc.y() * otherLoc.location().y(), bukkitLoc.z() * otherLoc.location().z(),
                            bukkitLoc.getYaw(), bukkitLoc.getPitch()));
            case Vector3 otherVec ->
                    new Location(new org.bukkit.Location(bukkitLoc.getWorld(), bukkitLoc.x() * otherVec.vector().x, bukkitLoc.y() * otherVec.vector().y, bukkitLoc.z() * otherVec.vector().z,
                            bukkitLoc.getYaw(), bukkitLoc.getPitch()));
            case com.bindglam.origami.api.script.interpreter.value.primitive.Number otherNum ->
                    new Location(bukkitLoc.clone().multiply(otherNum.value()));
            case null, default -> throw new UnsupportedOperationException(posStart, posEnd, context);
        };
    }

    @Override
    public Location divedBy(Value<?> other) throws ScriptException {
        return switch (other) {
            case LocationAdaptable otherLoc -> {
                if(Double.compare(otherLoc.location().x(), 0.0) == 0)
                    throw new DivisionByZeroException(posStart, posEnd, context);
                if(Double.compare(otherLoc.location().y(), 0.0) == 0)
                    throw new DivisionByZeroException(posStart, posEnd, context);
                if(Double.compare(otherLoc.location().z(), 0.0) == 0)
                    throw new DivisionByZeroException(posStart, posEnd, context);

                yield new Location(new org.bukkit.Location(bukkitLoc.getWorld(), bukkitLoc.x() / otherLoc.location().x(), bukkitLoc.y() / otherLoc.location().y(), bukkitLoc.z() / otherLoc.location().z(),
                        bukkitLoc.getYaw(), bukkitLoc.getPitch()));
            }
            case Vector3 otherVec -> {
                if(Double.compare(otherVec.vector().x, 0.0) == 0)
                    throw new DivisionByZeroException(posStart, posEnd, context);
                if(Double.compare(otherVec.vector().y, 0.0) == 0)
                    throw new DivisionByZeroException(posStart, posEnd, context);
                if(Double.compare(otherVec.vector().z, 0.0) == 0)
                    throw new DivisionByZeroException(posStart, posEnd, context);

                yield new Location(new org.bukkit.Location(bukkitLoc.getWorld(), bukkitLoc.x() / otherVec.vector().x, bukkitLoc.y() / otherVec.vector().y, bukkitLoc.z() / otherVec.vector().z,
                        bukkitLoc.getYaw(), bukkitLoc.getPitch()));
            }
            case com.bindglam.origami.api.script.interpreter.value.primitive.Number otherNum -> {
                if(Double.compare(otherNum.value(), 0.0) == 0)
                    throw new DivisionByZeroException(posStart, posEnd, context);

                yield new Location(new org.bukkit.Location(bukkitLoc.getWorld(), bukkitLoc.x() / otherNum.value(), bukkitLoc.y() / otherNum.value(), bukkitLoc.z() / otherNum.value(),
                        bukkitLoc.getYaw(), bukkitLoc.getPitch()));
            }
            case null, default -> throw new UnsupportedOperationException(posStart, posEnd, context);
        };
    }

    @Override
    public com.bindglam.origami.api.script.interpreter.value.primitive.Number compareEquals(Value<?> other) {
        if(!(other instanceof Location otherLoc)) return com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;

        return bukkitLoc.equals(otherLoc.bukkitLoc) ? com.bindglam.origami.api.script.interpreter.value.primitive.Number.TRUE : com.bindglam.origami.api.script.interpreter.value.primitive.Number.FALSE;
    }

    @Override
    public boolean isTrue() throws ScriptException {
        throw new UnsupportedOperationException(posStart, posEnd, context);
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
