package com.legs.appsforaa.installer

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuManager {
    private const val TAG = "ShizukuManager"
    const val SHIZUKU_PACKAGE = "moe.shizuku.privileged.api"
    const val REQUEST_CODE_SHIZUKU = 1001

    fun isShizukuInstalled(context: Context): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    SHIZUKU_PACKAGE,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(SHIZUKU_PACKAGE, 0)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun isShizukuRunning(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            false
        }
    }

    fun hasShizukuPermission(): Boolean {
        return try {
            if (isShizukuRunning()) {
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun requestShizukuPermission() {
        try {
            if (isShizukuRunning()) {
                if (Shizuku.shouldShowRequestPermissionRationale()) {
                    Log.d(TAG, "Shizuku permission rationale should be shown")
                }
                Shizuku.requestPermission(REQUEST_CODE_SHIZUKU)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to request Shizuku permission", e)
        }
    }

    suspend fun installApkPrivileged(apkPath: String): Result<String> = withContext(Dispatchers.IO) {
        if (!hasShizukuPermission()) {
            return@withContext Result.failure(IllegalStateException("Shizuku izni verilmedi"))
        }

        try {
            // pm install -i com.android.vending installs APK with Google Play Store as installer!
            val command = "pm install -i com.android.vending -r \"$apkPath\""
            Log.d(TAG, "Executing Shizuku command: $command")

            val newProcessMethod = Shizuku::class.java.getDeclaredMethod(
                "newProcess",
                Array<String>::class.java,
                Array<String>::class.java,
                String::class.java
            ).apply { isAccessible = true }

            val process = newProcessMethod.invoke(null, arrayOf("sh", "-c", command), null, null) as Process
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            
            val output = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            while (errorReader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }

            val exitCode = process.waitFor()
            val resultStr = output.toString().trim()
            Log.d(TAG, "Shizuku install result (exit: $exitCode): $resultStr")

            if (exitCode == 0 && resultStr.contains("Success", ignoreCase = true)) {
                Result.success(resultStr)
            } else {
                Result.failure(Exception("Yükleme başarısız (Kod $exitCode): $resultStr"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Shizuku install error", e)
            Result.failure(e)
        }
    }
}
