package co.earthme.fqbot.command

import co.earthme.fqbot.callbacks.NullContinuation
import co.earthme.fqbot.eventsystem.Listener
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.startCoroutine

class CommandParser : Listener{
    companion object {
        private val logger: Logger = LogManager.getLogger()
        private val currListener: AtomicReference<Bot> = AtomicReference()

        fun processEvent(event: Event) {
            if (event is MessageEvent) {
                if (event is GroupMessageEvent){
                    if (currListener.get() == null || !currListener.get().isOnline) {
                        currListener.set(event.bot)
                    }
                    if (currListener.get().equals(event.bot)) {
                        val processTask: suspend () -> Unit = {
                            fireProcess(event)
                        }
                        processTask.startCoroutine(NullContinuation())
                    }
                }else {
                    val processTask: suspend () -> Unit = {
                        fireProcess(event)
                    }
                    processTask.startCoroutine(NullContinuation())
                }
            }
        }

        private suspend fun fireProcess(event: MessageEvent) {
            val commandInfo = PackagedCommandInfo(event.message)
            if (commandInfo.getHead() == null) {
                return
            }
            logger.info("Processing command:{}", commandInfo)
            commandInfo.getHead()?.let {
                CommandList.search(it)?.process(commandInfo, event)
            }
        }
    }

    override fun onEvent(event: Event): Boolean {
        processEvent(event)
        return true
    }

    override fun listenerName(): String {
        return "CommandProcessor";
    }
}