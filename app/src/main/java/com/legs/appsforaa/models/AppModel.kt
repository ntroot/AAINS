package com.legs.appsforaa.models

enum class AppCategory(val title: String) {
    MULTIMEDIA("Medya & Video"),
    MIRRORING("Ekran Yansıtma"),
    TELEMETRY("Telemetri & Araç"),
    UTILITIES("Araçlar & Diğer")
}

enum class InstallStatus {
    NOT_INSTALLED,
    INSTALLED,
    UPDATE_AVAILABLE,
    DOWNLOADING,
    INSTALLING
}

data class AppModel(
    val id: String,
    val name: String,
    val packageName: String,
    val category: AppCategory,
    val description: String,
    val downloadUrl: String,
    val versionName: String,
    val minSdk: Int = 24,
    val githubUrl: String = "",
    var status: InstallStatus = InstallStatus.NOT_INSTALLED,
    var installedVersion: String? = null,
    var progress: Int = 0
)
