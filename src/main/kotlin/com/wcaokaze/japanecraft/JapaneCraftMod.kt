package com.wcaokaze.japanecraft

import kotlinx.coroutines.experimental.launch
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.NetworkCheckHandler
import net.minecraftforge.fml.relauncher.Side
import java.util.*

@Mod(modid = "japanecraft", version = "1.1.5")
class JapaneCraftMod {
  private val kanjiConverter = KanjiConverter()

  @Mod.EventHandler
  fun init(event: FMLInitializationEvent) {
    MinecraftForge.EVENT_BUS.register(this)
  }

  @SubscribeEvent
  fun onServerChat(event: ServerChatEvent) {
    launch {
      val (rawMessage, convertedMessage) = convert(event.message)

      val variableMap = mapOf(
          "n"                to "\n",
          "$"                to "\$",
          "username"         to event.username,
          "time"             to Configuration.timeFormatter.format(Date()),
          "rawMessage"       to rawMessage,
          "convertedMessage" to convertedMessage
      )

      Configuration.variableExpander.expand(variableMap).split('\n').forEach {
        event.player.sendMessage(TextComponentString(it))
      }
    }

    event.isCanceled = true
  }

  @NetworkCheckHandler
  fun netCheckHandler(mods: Map<String, String>, side: Side): Boolean {
    return true
  }

  private fun convert(message: String): Pair<String, String> {
    if (message.any { it >= 0x80.toChar() }) return "" to message

    val enMsg = message
    val jpMsg = enMsg.toJapanese()

    if (enMsg.filter { it != '`' } == jpMsg) return "" to enMsg

    return enMsg to jpMsg
  }

  private fun String.toJapanese(): String {
    try {
      val dicAppliedStr = split('`')
          .asSequence()
          .mapIndexed { idx, str ->
            val dicAppliedChunk = if (idx % 2 == 1) {
              str
            } else {
              val romajiWords = Configuration.splitWords(str)

              val hiraganaWords = romajiWords.map {
                if (!it.matches(Configuration.romajiRegex)) {
                  it
                } else {
                  Configuration.romajiConverter(it)
                }
              }

              val dicAppliedWords = hiraganaWords.map {
                Configuration.dictionary(it)
              }

              dicAppliedWords.joinToString("")
            }

            if (idx == 0) dicAppliedChunk else '`' + dicAppliedChunk
          }
          .joinToString("")

      return if (Configuration.kanjiConverterEnabled) {
        kanjiConverter(dicAppliedStr)
      } else {
        dicAppliedStr.replace("`", "")
      }
    } catch (e: Exception) {
      return this
    }
  }
}
