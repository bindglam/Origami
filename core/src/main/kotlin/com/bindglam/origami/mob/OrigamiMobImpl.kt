package com.bindglam.origami.mob

import com.bindglam.origami.api.mob.LivingMob
import com.bindglam.origami.api.mob.OrigamiMob
import com.bindglam.origami.api.mob.MobProperties
import com.bindglam.origami.api.script.Script
import org.bukkit.Location

class OrigamiMobImpl(private val properties: MobProperties, private val script: Script) : OrigamiMob {
    override fun properties(): MobProperties = properties

    override fun script(): Script = script

    override fun spawn(location: Location): LivingMob {
        val entity = location.world.spawnEntity(location, properties.type())
        val mob = LivingMobImpl(this, entity)

        return mob
    }
}