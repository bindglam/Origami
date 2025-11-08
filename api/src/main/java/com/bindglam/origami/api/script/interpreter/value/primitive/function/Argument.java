package com.bindglam.origami.api.script.interpreter.value.primitive.function;

import org.jetbrains.annotations.NotNull;

public record Argument(String name, boolean isOptional) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private boolean isOptional = false;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder isOptional(boolean isOptional) {
            this.isOptional = isOptional;
            return this;
        }

        public @NotNull Argument build() {
            if(name == null)
                throw new IllegalStateException("name cannot be null");

            return new Argument(name, isOptional);
        }
    }
}
