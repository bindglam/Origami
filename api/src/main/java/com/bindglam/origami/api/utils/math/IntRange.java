package com.bindglam.origami.api.utils.math;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public record IntRange(int min, int max) implements Iterable<Integer> {
    public boolean contains(int value) {
        return value >= min && value <= max;
    }

    @Override
    public @NotNull Iterator<Integer> iterator() {
        return new Iterator<>() {
            private int currentValue = min-1;

            @Override
            public boolean hasNext() {
                return currentValue < max;
            }

            @Override
            public Integer next() {
                return ++currentValue;
            }
        };
    }
}
