package com.bindglam.origami.listeners

import com.bindglam.origami.api.script.interpreter.value.bukkit.Entity
import com.bindglam.origami.manager.MobManagerImpl
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EntityListener : Listener {
    @EventHandler
    fun EntityDamageByEntityEvent.onDamage() {
        MobManagerImpl.getLivingMob(entity).ifPresent { mob ->
            mob.script().eventRegistry.callEvent("DAMAGE_BY_ENTITY", listOf(Entity(damager)))
        }
    }
}