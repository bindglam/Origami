package com.bindglam.origami.listeners

import com.bindglam.origami.manager.MobManagerImpl
import com.bindglam.origami.mob.OrigamiMobImpl
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.world.ChunkLoadEvent

object EntityListener : Listener {
    @EventHandler
    fun ChunkLoadEvent.onSpawn() {
        chunk.entities.forEach { entity ->
            if(!MobManagerImpl.mobs.contains(entity.uniqueId)) return@forEach

            val mob = OrigamiMobImpl()
        }
    }
}