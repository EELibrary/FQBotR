package co.earthme.fqbot

import co.earthme.fqbot.command.CommandList
import co.earthme.fqbot.command.CommandParser
import co.earthme.fqbot.command.commandentry.DebugCommand
import co.earthme.fqbot.command.commandentry.RPIC3Command
import co.earthme.fqbot.command.commandentry.RPICCommand
import co.earthme.fqbot.command.commandentry.ReloadCommand
import co.earthme.fqbot.eventsystem.EventHub
import co.earthme.fqbot.manager.BotManager
import co.earthme.fqbot.manager.ConfigManager
import co.earthme.fqbot.manager.DataManager
import co.earthme.fqbot.scripting.JSCommandLoader
import java.io.File

object Bootstrapper {
    val BASE_DIR = File("fqbot")

    fun shutdownBot(){
        BotManager.shutdownAllBot()
        CommandList.clearAll()
        EventHub.shutdownDispatcher()
        JSCommandLoader.clearAll()
    }

    fun runBot(){
        val scriptFolder = File(Bootstrapper.BASE_DIR, "jsscripts")

        if (!scriptFolder.exists()){
            scriptFolder.mkdir()
        }

        ConfigManager.initConfig()
        DataManager.initOrRead()
        JSCommandLoader.loadAll(scriptFolder)
        EventHub.register(CommandParser)
        BotManager.readConfig()
        BotManager.initAllBot()

        //Add shutdown hook
        Runtime.getRuntime().addShutdownHook(Thread {
            EventHub.shutdownDispatcher()
            JSCommandLoader.clearAll()
        })
        //Register commands

        CommandList.regCommand(RPIC3Command())
        CommandList.regCommand(RPICCommand())
        CommandList.regCommand(ReloadCommand())
        CommandList.regCommand(DebugCommand())
    }
}