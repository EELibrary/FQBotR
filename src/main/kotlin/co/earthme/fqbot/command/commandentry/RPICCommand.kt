package co.earthme.fqbot.command.commandentry

import co.earthme.fqbot.command.PackagedCommandInfo
import co.earthme.fqbot.manager.ConfigManager
import co.earthme.fqbot.utils.PixivRandomPictureResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.ByteArrayInputStream
import java.util.stream.Stream
import kotlin.streams.toList

class RPICCommand : CommandEntry {
    override fun getName(): String {
        return "rpic"
    }

    override suspend fun process(commandArg: PackagedCommandInfo, firedEvent: MessageEvent) {
        val target = getSenderForFeedback(firedEvent)

        val args = commandArg.getArgs()
        var gotLinks : Stream<String>? = null

        if (args.isNotEmpty()){
            if (args.size == 1){
                try {
                    val r18 = Integer.parseInt(args[0])
                    val got = PixivRandomPictureResponse.getNewLink(r18,1)
                    gotLinks = got?.let {
                        PixivRandomPictureResponse.getAllLinks(it)
                    }
                }catch (e : NumberFormatException){
                    target.sendMessage("Please support a int in the first arg!")
                }
            }
        }else{
            val got = PixivRandomPictureResponse.getNewLink(0,1)
            gotLinks = got?.let {
                PixivRandomPictureResponse.getAllLinks(it)
            }
        }

        val proxy = ConfigManager.getReadConfig().getReadProxy()
        for (link in gotLinks!!.toList()){
            val downloaded: ByteArray = if (proxy != null){
                PixivRandomPictureResponse.downloadFromLink(link, proxy)
            }else{
                PixivRandomPictureResponse.downloadFromLink(link)
            }

            downloaded.let {
                val picStream = ByteArrayInputStream(downloaded)
                try {
                    val image = picStream.uploadAsImage(target,null)
                    target.sendMessage(image)
                }finally {
                    withContext(Dispatchers.IO) {
                        picStream.close()
                    }
                }
            }
        }
    }
}