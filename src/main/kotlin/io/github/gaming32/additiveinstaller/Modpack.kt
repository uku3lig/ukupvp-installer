package io.github.gaming32.additiveinstaller

import com.google.gson.JsonElement
import java.util.*
import javax.imageio.ImageIO

private const val PROJECT_BASE = "https://api.modrinth.com/v2/project"

class Modpack(val id: String, val name: String) {
    val versions: Map<String, Map<String, Map<Loader, PackVersion>>> =
        requestCriticalJson("$PROJECT_BASE/$id/version").asJsonArray
            .asSequence()
            .map(JsonElement::getAsJsonObject)
            .map { PackVersion(this, it) }
            .run {
                val result = mutableMapOf<String, MutableMap<String, MutableMap<Loader, PackVersion>>>()
                for (version in this) {
                    val byPackVersion = result.getOrPut(version.gameVersion, ::mutableMapOf)
                    val byLoader = byPackVersion.getOrPut(version.packVersion) { EnumMap(Loader::class.java) }
                    byLoader[version.loader] = version
                }
                result
            }

    val windowTitle = I18N.getString("window.title", name)
    val banner = ImageIO.read(javaClass.getResource("/${id}_banner.png"))!!
    val appIcon = ImageIO.read(javaClass.getResource("/${id}_appicon.png"))!!
    val launcherIcon = javaClass.getResource("/${id}_icon.png")
        ?.readBytes()
        ?.toBase64()
        ?.prefix("data:image/png;base64,")
}
