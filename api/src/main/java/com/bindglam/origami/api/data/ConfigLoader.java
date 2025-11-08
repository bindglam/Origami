package com.bindglam.origami.api.data;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface ConfigLoader {
    static <T> @NotNull T load(ConfigurationSection config, Class<T> clazz) {
        try {
            Method loadMethod = clazz.getDeclaredMethod("load", ConfigurationSection.class);

            return clazz.cast(loadMethod.invoke(null, config));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
