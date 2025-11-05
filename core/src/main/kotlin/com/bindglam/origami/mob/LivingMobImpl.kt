package com.bindglam.origami.mob

import com.bindglam.origami.api.mob.LivingMob
import com.bindglam.origami.api.mob.OrigamiMob
import com.bindglam.origami.api.script.RuntimeScript
import com.bindglam.origami.manager.MobManagerImpl
import org.bukkit.entity.Entity

class LivingMobImpl(private val origamiMob: OrigamiMob, private val entity: Entity) : LivingMob {
    private val script = origamiMob.script().execute()

    override fun remove() {
        MobManagerImpl.mobs.remove(entity.uniqueId)

        entity.remove()

        script.close()
    }

    override fun script(): RuntimeScript = script

    override fun entity(): Entity = entity
}