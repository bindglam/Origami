package com.bindglam.origami

import com.bindglam.origami.api.Origami
import com.bindglam.origami.api.OrigamiProvider
import com.bindglam.origami.api.manager.ScriptManager
import com.bindglam.origami.listeners.EntityListener
import com.bindglam.origami.manager.MobManagerImpl
import com.bindglam.origami.manager.ScriptManagerImpl
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandAPIPaperConfig
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin

class OrigamiPlugin : JavaPlugin(), Origami {
    private val managers = listOf(
        ScriptManagerImpl,
        MobManagerImpl
    )

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIPaperConfig(this))

        CommandAPICommand("origami")
            .withPermission(CommandPermission.OP)
            .withSubcommands(
                CommandAPICommand("spawn")
                    .withArguments(StringArgument("id"))
                    .executesPlayer(PlayerCommandExecutor { player, args ->
                        val id = args["id"] as String

                        MobManagerImpl.getMob(id).ifPresentOrElse({ mob ->
                            mob.spawn(player.location)
                        }) {
                            player.sendMessage(Component.text("Unknown mob").color(NamedTextColor.RED))
                        }
                    })
            )
            .register()
    }

    override fun onEnable() {
        CommandAPI.onEnable()

        OrigamiProvider.register(this)

        server.pluginManager.registerEvents(EntityListener, this)

        managers.forEach { it.start() }
    }

    override fun onDisable() {
        CommandAPI.onDisable()

        managers.forEach { it.end() }
    }

    override fun scriptManager(): ScriptManager = ScriptManagerImpl
}