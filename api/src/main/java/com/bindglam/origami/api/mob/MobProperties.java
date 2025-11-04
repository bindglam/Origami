package com.bindglam.origami.api.mob;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public record MobProperties(String id, EntityType type) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private EntityType type = EntityType.ZOMBIE;

        private Builder() {
        }

        public Builder id(@NotNull String id) {
            this.id = id;
            return this;
        }

        public Builder type(@NotNull EntityType type) {
            this.type = type;
            return this;
        }

        public @NotNull MobProperties build() {
            return new MobProperties(id, type);
        }
    }
}
