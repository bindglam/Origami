package com.bindglam.origami.api.mob.properties;

import com.bindglam.origami.api.data.ConfigLoader;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record MobProperties(@NotNull String id, @NotNull EntityType type, Component displayName, Attributes attributes) implements ConfigLoader {
    public static @NotNull MobProperties load(ConfigurationSection config) {
        MobProperties.Builder properties = MobProperties.builder()
                .id(config.getName())
                .type(EntityType.valueOf(config.getString("type")))
                .displayName(config.getRichMessage("display-name"));

        if(config.contains("attributes")) {
            properties.attributes(ConfigLoader.load(config.getConfigurationSection("attributes"), Attributes.class));
        }

        return properties.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private EntityType type = EntityType.ZOMBIE;
        private Component displayName;
        private Attributes attributes;

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

        public Builder attributes(Attributes attributes) {
            this.attributes = attributes;
            return this;
        }

        public @NotNull MobProperties build() {
            if(id == null)
                throw new IllegalStateException("id cannot be null");
            if(type == null)
                throw new IllegalStateException("type cannot be null");

            return new MobProperties(id, type, displayName, attributes);
        }
    }
}
