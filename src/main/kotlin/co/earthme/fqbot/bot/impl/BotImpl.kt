package co.earthme.fqbot.bot.impl

import co.earthme.fqbot.bot.BotEntry
import co.earthme.fqbot.eventsystem.EventHub
import net.mamoe.mirai.event.Event

class BotImpl : BotEntry() {

    override fun processEvent(event: Event) {
        EventHub.callEventAsync(event)
    }
}