package com.bindglam.origami.scheduler

import com.bindglam.origami.api.scheduler.Scheduler
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.TimeUnit

class FoliaScheduler(private val plugin: JavaPlugin) : Scheduler {
    override fun task(action: Runnable) {
        Bukkit.getGlobalRegionScheduler().run(plugin) { _ -> action.run() }
    }

    override fun taskLater(action: Runnable, tick: Long) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, { _ -> action.run() }, tick)
    }

    override fun taskTimer(action: Runnable, delay: Long, interval: Long) {
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, { _ -> action.run() }, delay, interval)
    }

    override fun taskRegion(action: Runnable, location: Location) {
        Bukkit.getRegionScheduler().run(plugin, location) { _ -> action.run() }
    }

    override fun taskRegionLater(action: Runnable, location: Location, tick: Long) {
        Bukkit.getRegionScheduler().runDelayed(plugin, location, { _ -> action.run() }, tick)
    }

    override fun taskRegionTimer(action: Runnable, location: Location, delay: Long, interval: Long) {
        Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, { _ -> action.run() }, delay, interval)
    }

    override fun taskAsync(action: Runnable) {
        Bukkit.getAsyncScheduler().runNow(plugin) { _ -> action.run() }
    }

    override fun taskAsyncLater(action: Runnable, tick: Long) {
        Bukkit.getAsyncScheduler().runDelayed(plugin, { _ -> action.run() }, tick * Scheduler.TICK_MILLIS, TimeUnit.MILLISECONDS)
    }

    override fun taskAsyncTimer(action: Runnable, delay: Long, interval: Long) {
        Bukkit.getAsyncScheduler().runAtFixedRate(plugin, { _ -> action.run() }, delay * Scheduler.TICK_MILLIS, interval * Scheduler.TICK_MILLIS, TimeUnit.MILLISECONDS)
    }
}