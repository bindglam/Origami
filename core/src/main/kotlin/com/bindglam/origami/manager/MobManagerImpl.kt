package com.bindglam.origami.manager

import com.bindglam.origami.api.manager.MobManager
import com.bindglam.origami.api.mob.LivingMob
import com.bindglam.origami.api.mob.MobProperties
import com.bindglam.origami.api.mob.OrigamiMob
import com.bindglam.origami.mob.LivingMobImpl
import com.bindglam.origami.mob.OrigamiMobImpl
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.persistence.PersistentDataType
import java.io.File
import java.util.Optional
import java.util.UUID

object MobManagerImpl : MobManager {
    private val mobsFolder = File("plugins/Origami/mobs")

    val loadedMobs = hashMapOf<String, OrigamiMob>()

    val mobs = hashMapOf<UUID, LivingMob>()

    override fun start() {
        fun loadMob(id: String, config: ConfigurationSection): OrigamiMob {
            val properties = MobProperties.builder()
                .id(id)
                .type(EntityType.valueOf(config.getString("type")!!))

            val script = ScriptManagerImpl.getScript(if(config.contains("script")) config.getString("script")!! else id)

            return OrigamiMobImpl(properties.build(), script.orElseThrow())
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

    override fun getMob(id: String): Optional<OrigamiMob> = Optional.ofNullable(loadedMobs[id])

    override fun getLivingMob(entity: Entity): Optional<LivingMob> = Optional.ofNullable(mobs[entity.uniqueId])

    override fun isLivingMob(entity: Entity): Boolean = mobs.contains(entity.uniqueId)
}