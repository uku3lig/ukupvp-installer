package io.github.gaming32.additiveinstaller

import com.google.gson.JsonObject
import java.nio.file.Path

class PackVersion(val modpack: Modpack, val data: JsonObject) {
    val packVersion: String
    val gameVersion: String
    val loader: Loader

    init {
        val versionNumber = data["version_number"].asString
        packVersion = versionNumber.substringBefore('+')
        gameVersion = versionNumber.substringAfter("+mc")
        loader = data["loaders"].asJsonArray[0].asString.uppercase().let(Loader::valueOf)
    }

    val launcherFolderPath = "${modpack.id}/$packVersion-$gameVersion-$loader"
    val launcherVersionId = "${modpack.id}-$packVersion-$gameVersion-$loader"
    val launcherProfileId = "${modpack.id}-$gameVersion-$loader"
    val isSupported = true

    fun install(destination: Path, progressHandler: ProgressHandler) =
        PackInstaller(this, destination, progressHandler)
            .use(PackInstaller::install)
}
