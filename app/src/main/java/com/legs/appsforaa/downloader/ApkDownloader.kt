package com.legs.appsforaa.downloader

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

object ApkDownloader {
    private const val TAG = "ApkDownloader"

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    /**
     * Downloads an APK from the given URL and reports progress (0-100).
     * Saves to app's cache directory under "apks".
     */
    suspend fun downloadApk(
        context: Context,
        url: String,
        fileName: String,
        onProgress: (percent: Int) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val downloadDir = File(context.cacheDir, "apks").apply {
                if (!exists()) mkdirs()
            }
            val destinationFile = File(downloadDir, fileName)
            if (destinationFile.exists()) {
                destinationFile.delete()
            }

            Log.d(TAG, "Starting download: $url -> ${destinationFile.absolutePath}")

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Android; Mobile; rv:109.0) Gecko/114.0 Firefox/114.0")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("HTTP indirme hatası: ${response.code} ${response.message}"))
            }

            val body = response.body ?: return@withContext Result.failure(Exception("İçerik boş döndü"))
            val contentLength = body.contentLength()
            var bytesCopied = 0L

            body.byteStream().use { input ->
                FileOutputStream(destinationFile).use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    var lastPercent = -1

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        bytesCopied += bytesRead

                        if (contentLength > 0) {
                            val percent = ((bytesCopied * 100) / contentLength).toInt().coerceIn(0, 100)
                            if (percent != lastPercent) {
                                lastPercent = percent
                                onProgress(percent)
                            }
                        }
                    }
                    output.flush()
                }
            }

            // Doğrulama: İndirilen dosya geçerli bir APK mı?
            val packageArchiveInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageArchiveInfo(
                    destinationFile.absolutePath,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageArchiveInfo(destinationFile.absolutePath, 0)
            }

            if (packageArchiveInfo == null) {
                destinationFile.delete()
                return@withContext Result.failure(Exception("İndirilen dosya bozuk veya geçerli bir Android APK paketi değil."))
            }

            Log.d(TAG, "Download finished successfully: ${destinationFile.absolutePath}, package: ${packageArchiveInfo.packageName}")
            Result.success(destinationFile)
        } catch (e: Exception) {
            Log.e(TAG, "Download failed", e)
            Result.failure(e)
        }
    }

    /**
     * Cleans up all cached APK files to save storage space.
     */
    fun clearApkCache(context: Context) {
        try {
            val downloadDir = File(context.cacheDir, "apks")
            if (downloadDir.exists()) {
                downloadDir.deleteRecursively()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear APK cache", e)
        }
    }
}
