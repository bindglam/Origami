package com.bindglam.origami.api.mob.properties;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record MobProperties(String id, EntityType type, @Nullable Component displayName) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private EntityType type = EntityType.ZOMBIE;
        private Component displayName;

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

        public Builder displayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public @NotNull MobProperties build() {
            if(id == null)
                throw new IllegalStateException("id cannot be null");
            if(type == null)
                throw new IllegalStateException("type cannot be null");

            return new MobProperties(id, type, displayName);
        }
    }
}
