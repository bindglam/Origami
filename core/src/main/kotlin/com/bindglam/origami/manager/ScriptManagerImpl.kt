package com.bindglam.origami.manager

import com.bindglam.origami.api.manager.ScriptManager
import com.bindglam.origami.api.script.Script
import java.io.File
import java.util.Optional
import java.util.logging.Logger

object ScriptManagerImpl : ScriptManager {
    private val scriptsFolder = File("plugins/Origami/scripts")

    private val logger = Logger.getLogger("Origami ScriptManager")

    private val loadedScripts = hashMapOf<String, Script>()

    override fun start() {
        if(!scriptsFolder.exists())
            scriptsFolder.mkdirs()

        scriptsFolder.walkBottomUp().forEach { file ->
            if(file.isDirectory) return@forEach

            loadedScripts[file.nameWithoutExtension] = Script(logger, file)
        }

        compileAll()
    }

    override fun end() {
        loadedScripts.clear()
    }

    override fun compileAll() {
        logger.info("Compiling scripts...")

        loadedScripts.forEach { id, script -> script.compile() }

        logger.info("Successfully compiled all scripts!")
    }

    override fun getScript(id: String): Optional<Script> = Optional.ofNullable(loadedScripts[id])
}