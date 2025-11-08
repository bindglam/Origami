package com.bindglam.origami.api.mob.properties;

import com.bindglam.origami.api.data.ConfigLoader;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record Attributes(Map<Attribute, AttributeModifier> attributes) implements ConfigLoader {
    public static @NotNull Attributes load(ConfigurationSection config) {
        Attributes.Builder attributes = Attributes.builder();

        config.getKeys(false).forEach((id) -> {
            NamespacedKey key = Objects.requireNonNull(NamespacedKey.fromString(id));

            attributes.attribute(RegistryAccess.registryAccess().getRegistry(RegistryKey.ATTRIBUTE).getOrThrow(key), new AttributeModifier(key, config.getDouble(id), AttributeModifier.Operation.ADD_NUMBER));
        });

        return attributes.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<Attribute, AttributeModifier> attributes = new HashMap<>();

        private Builder() {
        }

        public Builder attribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
            this.attributes.put(attribute, modifier);
            return this;
        }

        public @NotNull Attributes build() {
            return new Attributes(attributes);
        }
    }
}
