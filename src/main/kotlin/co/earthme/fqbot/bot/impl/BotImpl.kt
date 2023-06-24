package co.earthme.fqbot.bot.impl

import co.earthme.fqbot.bot.BotEntry
import co.earthme.fqbot.command.CommandParser
import co.earthme.fqbot.eventsystem.EventHub
import net.mamoe.mirai.event.Event

class BotImpl : BotEntry() {
    override fun processEvent(event: Event?) {
        event?.let {
            eventHub.callEventAsync(event)
        }
    }

    companion object{
        private val eventHub: EventHub = EventHub()

        init {
            Runtime.getRuntime().addShutdownHook(Thread {
                eventHub.shutdownDispatcher()
            })

            eventHub.register(CommandParser())
        }

        fun getEventHub() : EventHub{
            return eventHub
        }
    }
}