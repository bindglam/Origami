package com.bindglam.origami.manager

import com.bindglam.origami.api.data.ConfigLoader
import com.bindglam.origami.api.manager.MobManager
import com.bindglam.origami.api.mob.LivingMob
import com.bindglam.origami.api.mob.properties.MobProperties
import com.bindglam.origami.api.mob.OrigamiMob
import com.bindglam.origami.api.mob.properties.Attributes
import com.bindglam.origami.mob.OrigamiMobImpl
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import java.io.File
import java.util.Optional
import java.util.UUID

object MobManagerImpl : MobManager {
    private val mobsFolder = File("plugins/Origami/mobs")

    val loadedMobs = hashMapOf<String, OrigamiMob>()

    val mobs = hashMapOf<UUID, LivingMob>()

    override fun start() {
        fun loadMob(id: String, config: ConfigurationSection): OrigamiMob {
            val script = ScriptManagerImpl.getScript(if(config.contains("script")) config.getString("script")!! else id)

            return OrigamiMobImpl(ConfigLoader.load(config, MobProperties::class.java), script.orElseThrow())
        }

        if(!mobsFolder.exists())
            mobsFolder.mkdirs()

        mobsFolder.walkBottomUp().forEach { file ->
            if(file.isDirectory) return@forEach

            val config = YamlConfiguration.loadConfiguration(file)

            config.getKeys(false).forEach { id ->
                loadedMobs[id] = loadMob(id, config.getConfigurationSection(id)!!)
            }
        }
    }

    override fun end() {
        loadedMobs.clear()
        mobs.clear()
    }

    override fun reload() {
        end()

        start()
    }

    override fun getMob(id: String): Optional<OrigamiMob> = Optional.ofNullable(loadedMobs[id])

    override fun getLivingMob(entity: Entity): Optional<LivingMob> = Optional.ofNullable(mobs[entity.uniqueId])

    override fun isLivingMob(entity: Entity): Boolean = mobs.contains(entity.uniqueId)
}