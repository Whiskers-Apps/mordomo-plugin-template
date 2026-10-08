import lib.CheckInput
import lib.CopyImage
import lib.CopyText
import lib.Entry
import lib.Form
import lib.FormResults
import lib.GetEntries
import lib.NumberInput
import lib.OpenUrl
import lib.PathInput
import lib.Plugin
import lib.PluginHandler
import lib.RunAction
import lib.SelectInput
import lib.SelectOption
import lib.ShowEntries
import lib.TextInput
import lib.getCheckResult
import lib.getJarPath
import lib.getNumberResult
import lib.getPathResult
import lib.getSelectResult
import lib.getTextResult
import java.io.File

suspend fun main() {
    val pluginHandler = PluginHandler("your-plugin-id")
    val iconsDir = File(getJarPath(), "icons")
    val bulbasaurIcon = File(iconsDir, "bulbasaur.png").absolutePath
    val shinyBulbasaurIcon = File(iconsDir, "bulbasaur-shiny.png").absolutePath

    pluginHandler.listen { message, pluginSettings, darkMode ->
        when (message) {
            is GetEntries -> {
                val searchText = message.searchText

                val entries = listOf(
                    Entry(
                        image = if (darkMode) shinyBulbasaurIcon else bulbasaurIcon,
                        title = "Image",
                        description = "This shows a bulbasaur and a shiny version if it's a dark theme"
                    ),
                    Entry(
                        title = "Test Copy Text",
                        action = CopyText("lorem ipsum")
                    ),
                    Entry(
                        title = "Test Copy Image",
                        action = CopyImage("/home/lighttigerxiv/Pictures/profile/tiger.jpg")
                    ),
                    Entry(
                        title = "Test Open Url",
                        action = OpenUrl(
                            "https://noai.duckduckgo.com/&q=lorem ipsum"
                        )
                    ),
                    Entry(
                        title = "Test Show Entries",
                        action = ShowEntries(
                            listOf(
                                Entry(title = "1", action = CopyText("1")),
                                Entry(title = "2", action = CopyText("2")),
                                Entry(title = "3", action = CopyText("3")),
                            )
                        )
                    ),
                    Entry(
                        title = "Custom Plugin Action",
                        action = Plugin(
                            pluginId = "your-plugin-id",
                            action = "send-notification",
                        )
                    ),
                    Entry(
                        title = "Show Plugin Settings",
                        action = Plugin(
                            pluginId = "your-plugin-id",
                            action = "show-settings",
                        )
                    ),
                    Entry(
                        title = "Fill a form",
                        action = Form(
                            pluginId = "your-plugin-id",
                            title = "Fill This Random Form",
                            buttonText = "Fill",
                            inputs = listOf(
                                TextInput(
                                    id = "name",
                                    title = "Name",
                                    description = "Input your name here",
                                    value = "",
                                ),
                                NumberInput(
                                    id = "age",
                                    title = "Age",
                                    description = "Input your age here",
                                    value = 18,
                                ),
                                CheckInput(
                                    id = "like_cats",
                                    title = "Like cats",
                                    description = "Check this if you like cats",
                                    value = true
                                ),
                                SelectInput(
                                    id = "fav_pokemon",
                                    title = "Fav Pokemon",
                                    description = "Select a pokemon",
                                    value = "oshawott",
                                    options = listOf(
                                        SelectOption(id = "oshawott", "Oshawott"),
                                        SelectOption(id = "tepig", "Tepig"),
                                        SelectOption(id = "snivy", "Snivy"),
                                    ),
                                ),
                                PathInput(
                                    id = "image",
                                    title = "Image",
                                    description = "Select a random image",
                                    fileExtensions = listOf("png", "jpg", "jpeg", "svg"),
                                ),
                                PathInput(
                                    id = "folder",
                                    title = "Folder",
                                    description = "Select a random folder",
                                    value = File(System.getProperty("user.home")).absolutePath,
                                    selectFolder = true
                                )
                            )
                        )
                    ),
                    Entry(
                        title = "Dangerous action",
                        description = "Press this to show a confirmation box",
                        action = CopyText("booo!!!", confirm = true)
                    ),
                    Entry(title = "You got this on text. $searchText")
                )

                pluginHandler.sendEntries(entries)
            }

            is RunAction -> {
                when (message.actionId) {
                    "send-notification" -> {
                        ProcessBuilder("notify-send", "Good Day", "Have a good day :D").start()
                    }

                    "show-settings" -> {
                        val username = pluginSettings["username"]
                        val age = pluginSettings["age"]
                        val eula = pluginSettings["eula"]
                        val starter = pluginSettings["starter"]

                        ProcessBuilder(
                            "notify-send",
                            "Username: $username\nAge: $age\nEula: $eula\nStarter: $starter"
                        ).start()
                    }
                }
            }

            is FormResults -> {
                val name = message.results.getTextResult("name")!!.value
                val age = message.results.getNumberResult("age")!!.value
                val likesCats = message.results.getCheckResult("like_cats")!!.value
                val favPokemon = message.results.getSelectResult("fav_pokemon")!!.value
                val imagePath = message.results.getPathResult("image")!!.value
                val folderPath = message.results.getPathResult("folder")!!.value

                ProcessBuilder(
                    "notify-send",
                    "Results",
                    "Name: $name\nAge: $age\nLikes Cats: $likesCats\nFavourite Pokemon: $favPokemon\nImage Path: $imagePath\nFolder Path: $folderPath"
                ).start()
            }
        }
    }
}
