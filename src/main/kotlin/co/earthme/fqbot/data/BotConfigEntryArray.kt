package co.earthme.fqbot.data

import co.earthme.fqbot.bot.BotConfigEntry
import com.google.gson.Gson

@Deprecated("Unsupported currently")
class BotConfigEntryArray(
    private val entries: Array<BotConfigEntry>
) {
    override fun toString(): String {
        return GSON.toJson(this)
    }

    fun getConfigEntries(): Array<BotConfigEntry> {
        return this.entries
    }

    companion object {
        private val GSON = Gson()
    }
}