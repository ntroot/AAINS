package com.legs.appsforaa.repository

import com.legs.appsforaa.models.AppCategory
import com.legs.appsforaa.models.AppModel

object AppCatalog {

    fun getDefaultApps(): List<AppModel> {
        return listOf(
            // Multimedia
            AppModel(
                id = "carstream",
                name = "CarStream",
                packageName = "maps.jaoloonda.android",
                category = AppCategory.MULTIMEDIA,
                description = "Android Auto için YouTube ve web tarayıcısı. Araç ekranında video izlemenizi sağlar.",
                downloadUrl = "https://firebasestorage.googleapis.com/v0/b/appsforaa-1b443.appspot.com/o/CarStream%2F2.0.5?alt=media&token=11545549-78bc-4cf0-bc0b-91e3b5a35bbd",
                versionName = "2.0.5",
                githubUrl = "https://github.com/thekirankumar/carstream-android-auto"
            ),
            AppModel(
                id = "fermata_auto",
                name = "Fermata Auto",
                packageName = "me.aap.fermata.auto.dear.google.why",
                category = AppCategory.MULTIMEDIA,
                description = "Android Auto için gelişmiş medya oynatıcı. Yerel video/ses, IPTV ve web tarayıcı desteği sunar.",
                downloadUrl = "https://github.com/AndreyPavlenko/Fermata/releases/download/2.0.2/fermata-auto-2.0.2-arm64.apk",
                versionName = "2.0.2",
                githubUrl = "https://github.com/AndreyPavlenko/Fermata"
            ),

            // Mirroring
            AppModel(
                id = "screen2auto",
                name = "Screen2Auto",
                packageName = "android.loandamaps.it",
                category = AppCategory.MIRRORING,
                description = "Android Auto için en gelişmiş ekran yansıtma uygulaması. Özel başlatıcı, dokunmatik ekran kontrolü ve çoklu pencere desteği sunar.",
                downloadUrl = "https://firebasestorage.googleapis.com/v0/b/appsforaa-1b443.appspot.com/o/s2a%2F3.7.apk?alt=media&token=658e85c3-22bc-44d3-be98-7a847c5b26e8",
                versionName = "3.7",
                githubUrl = "https://inceptive.ru/projects/s2a"
            ),
            AppModel(
                id = "aamirror",
                name = "AA Mirror",
                packageName = "maps.kiao2client.android",
                category = AppCategory.MIRRORING,
                description = "Telefonunuzun ekranını araç ekranına birebir yansıtan ilk ve klasik yansıtma aracı.",
                downloadUrl = "https://firebasestorage.googleapis.com/v0/b/appsforaa-1b443.appspot.com/o/mirror-debug.apk?alt=media&token=d1b2ae72-0c85-4d01-965c-033a27a7c9dc",
                versionName = "1.0",
                githubUrl = "https://github.com/slashmax/aamirror"
            ),
            AppModel(
                id = "aamirror_plus",
                name = "AA Mirror Plus",
                packageName = "maps.mobilejiohub.android",
                category = AppCategory.MIRRORING,
                description = "Gelişmiş dokunmatik ekran kontrolü ve geniş ekran desteğine sahip geliştirilmiş AA Mirror sürümü.",
                downloadUrl = "https://firebasestorage.googleapis.com/v0/b/appsforaa-1b443.appspot.com/o/AAMirrorPlus%2FAAMirror%20Plus%201.01a.apk?alt=media&token=0d6b0198-e74c-4532-900d-04086f0201ea",
                versionName = "1.01a",
                githubUrl = "https://github.com/slashmax/aamirror"
            ),
            AppModel(
                id = "aastream",
                name = "AA Stream",
                packageName = "maps.mobilejiohubclient.android",
                category = AppCategory.MIRRORING,
                description = "AAMirror alternatifi modern ekran yansıtma uygulaması.",
                downloadUrl = "https://firebasestorage.googleapis.com/v0/b/appsforaa-1b443.appspot.com/o/AAStream%2Faa-stream-v1.1.0.29-release.apk?alt=media&token=e80107f9-4b8f-4625-8a50-1f5236ffb3bf",
                versionName = "1.1.0",
                githubUrl = "https://github.com/endyrubbin/AAStream"
            ),

            // Telemetry & Vehicle
            AppModel(
                id = "aatorque",
                name = "AA Torque",
                packageName = "com.aatorque.stats",
                category = AppCategory.TELEMETRY,
                description = "Torque Pro OBD-II arıza tespit ve araç sensör verilerini doğrudan araç ekranında gösterir.",
                downloadUrl = "https://github.com/agronick/aa-torque/releases/download/2.0.30/aa-torque.apk",
                versionName = "2.0.30",
                githubUrl = "https://github.com/agronick/aa-torque"
            ),
            AppModel(
                id = "performance_monitor",
                name = "Performance Monitor",
                packageName = "com.mqbcoding.stats",
                category = AppCategory.TELEMETRY,
                description = "VAG (Volkswagen, Audi, Seat, Skoda) MIB2 üniteleri için turbo basıncı, yağ sıcaklığı gibi telemetri verilerini görüntüler.",
                downloadUrl = "https://github.com/jilleb/mqb-pm/releases/download/v0.88B/Performance.Monitor.v0.88B.apk",
                versionName = "0.88B",
                githubUrl = "https://github.com/jilleb/mqb-pm"
            ),

            // Utilities & Others
            AppModel(
                id = "aabrowser",
                name = "AA Browser",
                packageName = "com.kododake.aabrowser",
                category = AppCategory.UTILITIES,
                description = "Araç ekranı için optimize edilmiş, tam ekran video destekli modern web tarayıcısı.",
                downloadUrl = "https://github.com/kododake/AABrowser/releases/download/v3.0/AABrowser-3.0.apk",
                versionName = "3.0",
                githubUrl = "https://github.com/kododake/AABrowser"
            ),
            AppModel(
                id = "nav2contacts",
                name = "Nav2Contacts",
                packageName = "nl.frankkie.nav2contacts",
                category = AppCategory.UTILITIES,
                description = "Telefon rehberinizdeki kayıtlı adreslere 3. parti navigasyon uygulamalarıyla tek tıkla rota oluşturmanızı sağlar.",
                downloadUrl = "https://github.com/frankkienl/Nav2Contacts/releases/download/v1.0.3/Nav2Contatcs-release5_1.0.3.apk",
                versionName = "1.0.3",
                githubUrl = "https://github.com/frankkienl/Nav2Contacts"
            ),
            AppModel(
                id = "aawidgets",
                name = "Widgets for Android Auto",
                packageName = "de.nsvb.android.auto.w4a",
                category = AppCategory.UTILITIES,
                description = "Telefon ana ekranınızdaki widget'ları Android Auto araç ekranına taşır.",
                downloadUrl = "https://github.com/ns130291/widgets-for-auto/releases/download/v0.2.3/widgets4auto-0.2.3.apk",
                versionName = "0.2.3",
                githubUrl = "https://github.com/ns130291/widgets-for-auto"
            ),
            AppModel(
                id = "aapassenger",
                name = "AA Passenger",
                packageName = "com.github.martoreto.aapassenger",
                category = AppCategory.UTILITIES,
                description = "Araçtaki yolcuların Android Auto ses ve müzik kontrollerini yönetmesine izin veren uzaktan kontrol aracı.",
                downloadUrl = "https://github.com/martoreto/aapassenger/releases/download/1.9-alpha9/aapassenger-1.9-alpha9.apk",
                versionName = "1.9-alpha9",
                githubUrl = "https://github.com/martoreto/aapassenger"
            )
        )
    }
}
