package com.bindglam.origami.mob

import com.bindglam.origami.api.OrigamiProvider
import com.bindglam.origami.api.mob.LivingMob
import com.bindglam.origami.api.mob.OrigamiMob
import com.bindglam.origami.api.mob.MobProperties
import com.bindglam.origami.api.script.Script
import com.bindglam.origami.manager.MobManagerImpl
import com.bindglam.origami.utils.plugin
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType

class OrigamiMobImpl(private val properties: MobProperties, private val script: Script) : OrigamiMob {
    companion object {
        val PDC_MOB_ID = NamespacedKey(OrigamiProvider.origami().plugin(), "mob-id")
    }

    override fun properties(): MobProperties = properties

    override fun script(): Script = script

    override fun spawn(location: Location): LivingMob {
        val entity = location.world.spawnEntity(location, properties.type()).apply {
            persistentDataContainer.set(PDC_MOB_ID, PersistentDataType.STRING, properties.id())
        }

        val mob = LivingMobImpl(this, entity)

        MobManagerImpl.mobs[entity.uniqueId] = mob

        return mob
    }
}