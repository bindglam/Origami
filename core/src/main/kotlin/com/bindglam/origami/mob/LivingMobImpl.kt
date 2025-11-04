package com.bindglam.origami.mob

import com.bindglam.origami.api.mob.LivingMob
import com.bindglam.origami.api.mob.OrigamiMob
import org.bukkit.entity.Entity

class LivingMobImpl(private val origamiMob: OrigamiMob, private val entity: Entity) : LivingMob {
    private val script = origamiMob.script().execute()

    override fun remove() {
        entity.remove()

        script.close()
    }

    override fun entity(): Entity = entity
}