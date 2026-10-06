package com.legs.appsforaa.installer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

object InstallerEngine {
    private const val TAG = "InstallerEngine"

    suspend fun installApk(context: Context, apkFile: File): Result<String> {
        // Öncelik 1: Shizuku çalışıyorsa Android 14+ için en temiz ve garantili yöntem (Sessiz & rootless)
        if (ShizukuManager.isShizukuRunning() && ShizukuManager.hasShizukuPermission()) {
            Log.d(TAG, "Using Shizuku privileged installation")
            val shizukuResult = ShizukuManager.installApkPrivileged(apkFile.absolutePath)
            if (shizukuResult.isSuccess) {
                return shizukuResult
            }
            Log.w(TAG, "Shizuku install failed, falling back to PackageInstaller session", shizukuResult.exceptionOrNull())
        }

        // Öncelik 2: PackageInstaller Session ile com.android.vending (Play Store) kimliği atama
        return try {
            installViaPackageInstallerSession(context, apkFile)
            Result.success("Yükleme oturumu başlatıldı")
        } catch (e: Exception) {
            Log.w(TAG, "PackageInstaller session failed, falling back to Intent", e)
            try {
                installViaIntent(context, apkFile)
                Result.success("Sistem yükleyicisi açıldı")
            } catch (intentEx: Exception) {
                Log.e(TAG, "Intent install failed", intentEx)
                Result.failure(intentEx)
            }
        }
    }

    private suspend fun installViaPackageInstallerSession(context: Context, apkFile: File) = withContext(Dispatchers.IO) {
        val packageInstaller = context.packageManager.packageInstaller
        val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        
        // Android Auto'nun uygulamayı tanıması için com.android.vending (Google Play Store) olarak işaretle
        try {
            params.setInstallerPackageName("com.android.vending")
        } catch (e: Exception) {
            Log.w(TAG, "Could not setInstallerPackageName to com.android.vending", e)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.setInstallReason(PackageManager.INSTALL_REASON_USER)
        }

        val sessionId = packageInstaller.createSession(params)
        val session = packageInstaller.openSession(sessionId)

        session.openWrite("package", 0, apkFile.length()).use { out ->
            FileInputStream(apkFile).use { input ->
                input.copyTo(out)
            }
            session.fsync(out)
        }

        val callbackIntent = Intent(context, InstallReceiver::class.java).apply {
            action = InstallReceiver.ACTION_INSTALL_COMPLETE
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            sessionId,
            callbackIntent,
            flags
        )

        session.commit(pendingIntent.intentSender)
        session.close()
    }

    fun installViaIntent(context: Context, apkFile: File) {
        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "sksa.aa.customapps.fileProvider",
            apkFile
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(contentUri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, "com.android.vending")
        }

        context.startActivity(intent)
    }
}
