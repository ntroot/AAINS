package com.legs.appsforaa.ui

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.legs.appsforaa.AboutDialog
import com.legs.appsforaa.R
import com.legs.appsforaa.databinding.ActivityMainBinding
import com.legs.appsforaa.downloader.ApkDownloader
import com.legs.appsforaa.installer.InstallerEngine
import com.legs.appsforaa.installer.ShizukuManager
import com.legs.appsforaa.models.AppCategory
import com.legs.appsforaa.models.AppModel
import com.legs.appsforaa.models.InstallStatus
import com.legs.appsforaa.repository.AppCatalog
import com.legs.appsforaa.utils.Version
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AppAdapter

    private val allApps = mutableListOf<AppModel>()
    private var currentCategory: AppCategory? = null

    private val shizukuPermissionListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (requestCode == ShizukuManager.REQUEST_CODE_SHIZUKU) {
            updateShizukuBanner()
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(binding.root, "Shizuku izni başarıyla verildi!", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Shizuku izni reddedildi.", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private val shizukuBinderReceivedListener = Shizuku.OnBinderReceivedListener {
        runOnUiThread { updateShizukuBanner() }
    }

    private val shizukuBinderDeadListener = Shizuku.OnBinderDeadListener {
        runOnUiThread { updateShizukuBanner() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupCategoryChips()
        setupShizukuListeners()
        setupActions()

        loadApps()
    }

    override fun onResume() {
        super.onResume()
        updateShizukuBanner()
        refreshInstalledStatuses()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            Shizuku.removeRequestPermissionResultListener(shizukuPermissionListener)
            Shizuku.removeBinderReceivedListener(shizukuBinderReceivedListener)
            Shizuku.removeBinderDeadListener(shizukuBinderDeadListener)
        } catch (_: Exception) {
        }
    }

    private fun setupRecyclerView() {
        adapter = AppAdapter(
            onActionClick = { app -> handleAppInstallClick(app) },
            onWebsiteClick = { app -> openWebsite(app.githubUrl) }
        )
        binding.recyclerViewApps.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewApps.adapter = adapter
    }

    private fun setupCategoryChips() {
        binding.chipGroupCategories.setOnCheckedStateChangeListener { _, checkedIds ->
            currentCategory = when {
                checkedIds.contains(R.id.chipMultimedia) -> AppCategory.MULTIMEDIA
                checkedIds.contains(R.id.chipMirroring) -> AppCategory.MIRRORING
                checkedIds.contains(R.id.chipTelemetry) -> AppCategory.TELEMETRY
                checkedIds.contains(R.id.chipUtilities) -> AppCategory.UTILITIES
                else -> null
            }
            filterApps()
        }
    }

    private fun setupShizukuListeners() {
        try {
            Shizuku.addRequestPermissionResultListener(shizukuPermissionListener)
            Shizuku.addBinderReceivedListener(shizukuBinderReceivedListener)
            Shizuku.addBinderDeadListener(shizukuBinderDeadListener)
        } catch (e: Exception) {
            Log.w("MainActivity", "Shizuku listener registration skipped", e)
        }
    }

    private fun setupActions() {
        binding.btnAAGuide.setOnClickListener {
            showAAGuide()
        }

        binding.swipeRefresh.setOnRefreshListener {
            refreshInstalledStatuses()
            updateShizukuBanner()
            binding.swipeRefresh.isRefreshing = false
        }

        binding.btnShizukuAction.setOnClickListener {
            if (ShizukuManager.isShizukuRunning()) {
                if (!ShizukuManager.hasShizukuPermission()) {
                    ShizukuManager.requestShizukuPermission()
                } else {
                    Snackbar.make(binding.root, "Shizuku zaten tam yetkiyle çalışıyor!", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                openOrInstallShizuku()
            }
        }
    }

    private fun updateShizukuBanner() {
        val isRunning = ShizukuManager.isShizukuRunning()
        val hasPermission = ShizukuManager.hasShizukuPermission()

        when {
            isRunning && hasPermission -> {
                binding.cardShizuku.strokeColor = ContextCompat.getColor(this, R.color.md_theme_light_primary)
                binding.imgShizukuIcon.setImageResource(R.drawable.ic_bolt)
                binding.imgShizukuIcon.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.md_theme_light_primary)
                )
                binding.txtShizukuTitle.text = "Shizuku Etkin (Play Store İmzalı)"
                binding.txtShizukuDesc.text = "Android 14+ için sessiz ve garantili yükleme devrede."
                binding.btnShizukuAction.text = "Hazır"
                binding.btnShizukuAction.isEnabled = false
            }
            isRunning && !hasPermission -> {
                binding.cardShizuku.strokeColor = ContextCompat.getColor(this, R.color.status_update_text)
                binding.imgShizukuIcon.setImageResource(R.drawable.ic_bolt)
                binding.imgShizukuIcon.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.status_update_text)
                )
                binding.txtShizukuTitle.text = "Shizuku İzni Gerekli"
                binding.txtShizukuDesc.text = "Sessiz kurulum yapabilmek için lütfen izin verin."
                binding.btnShizukuAction.text = "Yetki Ver"
                binding.btnShizukuAction.isEnabled = true
            }
            else -> {
                binding.cardShizuku.strokeColor = ContextCompat.getColor(this, R.color.md_theme_light_outlineVariant)
                binding.imgShizukuIcon.setImageResource(R.drawable.ic_info)
                binding.imgShizukuIcon.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.md_theme_light_outline)
                )
                binding.txtShizukuTitle.text = "Standart Kurulum Modu"
                binding.txtShizukuDesc.text = "Android 14+ için Shizuku önerilir. Olmadığında standart oturum kullanılır."
                binding.btnShizukuAction.text = "Shizuku Aç"
                binding.btnShizukuAction.isEnabled = true
            }
        }
    }

    private fun openOrInstallShizuku() {
        val shizukuPackage = ShizukuManager.SHIZUKU_PACKAGE
        if (ShizukuManager.isShizukuInstalled(this)) {
            val launchIntent = packageManager.getLaunchIntentForPackage(shizukuPackage)
            if (launchIntent != null) {
                startActivity(launchIntent)
            } else {
                Toast.makeText(this, "Shizuku uygulaması açılamadı", Toast.LENGTH_SHORT).show()
            }
        } else {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$shizukuPackage")))
            } catch (_: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$shizukuPackage")))
            }
        }
    }

    private fun loadApps() {
        allApps.clear()
        allApps.addAll(AppCatalog.getDefaultApps())
        refreshInstalledStatuses()
    }

    private fun refreshInstalledStatuses() {
        val pm = packageManager
        for (app in allApps) {
            try {
                val pkgInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getPackageInfo(app.packageName, PackageManager.PackageInfoFlags.of(0))
                } else {
                    @Suppress("DEPRECATION")
                    pm.getPackageInfo(app.packageName, 0)
                }

                val installedVersion = pkgInfo.versionName ?: ""
                app.installedVersion = installedVersion

                val isNewer = try {
                    Version(app.versionName) > Version(installedVersion)
                } catch (_: Exception) {
                    false
                }

                if (isNewer) {
                    app.status = InstallStatus.UPDATE_AVAILABLE
                } else {
                    app.status = InstallStatus.INSTALLED
                }
            } catch (_: PackageManager.NameNotFoundException) {
                if (app.status != InstallStatus.DOWNLOADING && app.status != InstallStatus.INSTALLING) {
                    app.status = InstallStatus.NOT_INSTALLED
                }
                app.installedVersion = null
            }
        }
        filterApps()
    }

    private fun filterApps() {
        val filtered = if (currentCategory == null) {
            allApps.toList()
        } else {
            allApps.filter { it.category == currentCategory }
        }
        adapter.submitList(filtered.toList())
    }

    private fun handleAppInstallClick(app: AppModel) {
        if (app.status == InstallStatus.DOWNLOADING || app.status == InstallStatus.INSTALLING) {
            return
        }

        val previousStatus = app.status
        app.status = InstallStatus.DOWNLOADING
        app.progress = 0
        notifyAppChanged(app)

        lifecycleScope.launch {
            val fileName = "${app.id}_v${app.versionName}.apk"
            val downloadResult = ApkDownloader.downloadApk(
                context = this@MainActivity,
                url = app.downloadUrl,
                fileName = fileName,
                onProgress = { percent ->
                    runOnUiThread {
                        app.progress = percent
                        notifyAppProgressChanged(app)
                    }
                }
            )

            if (downloadResult.isFailure) {
                app.status = previousStatus
                notifyAppChanged(app)
                val errorMsg = downloadResult.exceptionOrNull()?.localizedMessage ?: "İndirme başarısız oldu"
                Snackbar.make(binding.root, "${app.name}: $errorMsg", Snackbar.LENGTH_LONG)
                    .setAction("Tekrar Dene") { handleAppInstallClick(app) }
                    .show()
                return@launch
            }

            val apkFile = downloadResult.getOrNull()!!
            app.status = InstallStatus.INSTALLING
            notifyAppChanged(app)

            val installResult = InstallerEngine.installApk(this@MainActivity, apkFile)

            if (installResult.isSuccess) {
                Snackbar.make(binding.root, "${app.name} yükleme işlemi başlatıldı!", Snackbar.LENGTH_LONG).show()
            } else {
                app.status = previousStatus
                notifyAppChanged(app)
                val errorMsg = installResult.exceptionOrNull()?.localizedMessage ?: "Yükleme başlatılamadı"
                Snackbar.make(binding.root, "${app.name}: $errorMsg", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun notifyAppChanged(app: AppModel) {
        val position = adapter.currentList.indexOfFirst { it.id == app.id }
        if (position != -1) {
            adapter.notifyItemChanged(position)
        }
    }

    private fun notifyAppProgressChanged(app: AppModel) {
        val position = adapter.currentList.indexOfFirst { it.id == app.id }
        if (position != -1) {
            adapter.notifyItemChanged(position, AppAdapter.PAYLOAD_PROGRESS)
        }
    }

    private fun openWebsite(url: String) {
        if (url.isBlank()) return
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            Toast.makeText(this, "Bağlantı açılamadı", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAAGuide() {
        AAGuideBottomSheet.newInstance().show(supportFragmentManager, AAGuideBottomSheet.TAG)
    }

    private fun showAboutDialog() {
        AboutDialog().show(supportFragmentManager, "AboutDialog")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.infos -> {
                showAboutDialog()
                true
            }
            R.id.help -> {
                showAAGuide()
                true
            }
            R.id.contact -> {
                try {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:ntroot@gmail.com")
                        putExtra(Intent.EXTRA_SUBJECT, "AAINS - Destek / Geri Bildirim")
                    }
                    startActivity(Intent.createChooser(intent, "E-posta Gönder"))
                } catch (e: Exception) {
                    Toast.makeText(this, "E-posta: ntroot@gmail.com", Toast.LENGTH_LONG).show()
                }
                true
            }
            R.id.clear_cache -> {
                ApkDownloader.clearApkCache(this)
                Snackbar.make(binding.root, "İndirme önbelleği temizlendi", Snackbar.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
