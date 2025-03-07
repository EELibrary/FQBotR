package co.earthme.fqbot.command.commandentry

import co.earthme.fqbot.command.PackagedCommandInfo
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent

interface CommandEntry {
    fun getName(): String

    suspend fun process(commandArg: PackagedCommandInfo, firedEvent: MessageEvent)

    fun getSenderForFeedback(msgEvent: MessageEvent): Contact{
        if (msgEvent is GroupMessageEvent){
            return msgEvent.group
        }

        return msgEvent.sender
    }
}