import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    id("paper-conventions")
    alias(libs.plugins.resourceFactory.paper)
}

dependencies {
    implementation(project(":api"))
    /*rootProject.project("nms").subprojects.forEach {
        implementation(project(":nms:${it.name}"))
    }*/

    implementation("dev.jorel:commandapi-paper-shade:11.0.0")
}

paperPluginYaml {
    name = rootProject.name
    version = rootProject.version.toString()
    main = "$group.OrigamiPlugin"
    apiVersion = "1.21"
    author = "Bindglam"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
}