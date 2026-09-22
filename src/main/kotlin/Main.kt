import lib.CopyImage
import lib.CopyText
import lib.Entry
import lib.FormResults
import lib.GetEntries
import lib.OpenUrl
import lib.Plugin
import lib.PluginHandler
import lib.RunAction
import lib.ShowEntries

suspend fun main() {
    val pluginHandler = PluginHandler("your-plugin-id")

    pluginHandler.listen { message ->
        when (message) {
            is GetEntries -> {
                val searchText = message.searchText

                val entries =  listOf(
                    Entry(title = "Test Copy Text", actions = listOf(CopyText("", "lorem ipsum"))),
                    Entry(
                        title = "Test Copy Image", actions = listOf(
                            CopyImage(
                                "",
                                "/home/lighttigerxiv/Pictures/profile/tiger.jpg"
                            )
                        )
                    ),
                    Entry(
                        title = "Test Open Url", actions = listOf(
                            OpenUrl(
                                "",
                                "https://noai.duckduckgo.com/&q=lorem ipsum"
                            )
                        )
                    ),
                    Entry(
                        title = "Test Show Entries", actions = listOf(
                            ShowEntries(
                                "", listOf(
                                    Entry(title = "1", actions = listOf(CopyText("", "1"))),
                                    Entry(title = "2", actions = listOf(CopyText("", "2"))),
                                    Entry(title = "3", actions = listOf(CopyText("", "3"))),
                                )
                            )
                        )
                    ),
                    Entry(title = "Custom Plugin Actions", actions = listOf(
                        Plugin(
                            text = "Send Notification",
                            pluginId = "your-plugin-id",
                            action = "send-notification",
                        )
                    )),
                    Entry(title = "You got this on text. $searchText")
                )

                pluginHandler.sendEntries(entries)
            }

            is RunAction -> {
                when(message.actionId){
                    "send-notification" -> {
                        ProcessBuilder("notify-send", "Good Day", "Have a good day :D").start()
                    }
                }
            }
            is FormResults -> {}
        }
    }
}