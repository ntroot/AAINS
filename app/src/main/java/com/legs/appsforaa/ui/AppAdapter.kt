package com.legs.appsforaa.ui

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.legs.appsforaa.R
import com.legs.appsforaa.databinding.ItemAppCardBinding
import com.legs.appsforaa.models.AppModel
import com.legs.appsforaa.models.InstallStatus

class AppAdapter(
    private val onActionClick: (AppModel) -> Unit,
    private val onWebsiteClick: (AppModel) -> Unit
) : ListAdapter<AppModel, AppAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] == PAYLOAD_PROGRESS) {
            holder.bindProgress(getItem(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class AppViewHolder(private val binding: ItemAppCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(app: AppModel) {
            val context = binding.root.context

            binding.txtAppName.text = app.name
            binding.txtAppVersion.text = "v${app.versionName}"
            binding.txtCategory.text = app.category.title
            binding.txtDescription.text = app.description

            // Website button
            if (app.githubUrl.isNotBlank()) {
                binding.btnWebsite.visibility = View.VISIBLE
                binding.btnWebsite.setOnClickListener { onWebsiteClick(app) }
            } else {
                binding.btnWebsite.visibility = View.GONE
            }

            // Primary action button listener
            binding.btnAction.setOnClickListener {
                if (app.status != InstallStatus.DOWNLOADING && app.status != InstallStatus.INSTALLING) {
                    onActionClick(app)
                }
            }

            updateStatusAndProgress(context, app)
        }

        fun bindProgress(app: AppModel) {
            val context = binding.root.context
            updateStatusAndProgress(context, app)
        }

        private fun updateStatusAndProgress(context: Context, app: AppModel) {
            when (app.status) {
                InstallStatus.NOT_INSTALLED -> {
                    binding.txtStatusBadge.text = "Kurulu Değil"
                    binding.txtStatusBadge.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_not_installed_bg)
                    )
                    binding.txtStatusBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.status_not_installed_text)
                    )

                    binding.layoutProgress.visibility = View.GONE
                    binding.btnAction.isEnabled = true
                    binding.btnAction.text = "İndir ve Kur"
                    binding.btnAction.setIconResource(R.drawable.ic_download)
                }
                InstallStatus.INSTALLED -> {
                    binding.txtStatusBadge.text = "Yüklü"
                    binding.txtStatusBadge.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_installed_bg)
                    )
                    binding.txtStatusBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.status_installed_text)
                    )

                    binding.layoutProgress.visibility = View.GONE
                    binding.btnAction.isEnabled = true
                    binding.btnAction.text = "Yeniden Kur"
                    binding.btnAction.setIconResource(R.drawable.ic_check_circle)
                }
                InstallStatus.UPDATE_AVAILABLE -> {
                    binding.txtStatusBadge.text = "Güncelleme Var"
                    binding.txtStatusBadge.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.status_update_bg)
                    )
                    binding.txtStatusBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.status_update_text)
                    )

                    binding.layoutProgress.visibility = View.GONE
                    binding.btnAction.isEnabled = true
                    binding.btnAction.text = "Güncelle"
                    binding.btnAction.setIconResource(R.drawable.ic_download)
                }
                InstallStatus.DOWNLOADING -> {
                    binding.txtStatusBadge.text = "İndiriliyor"
                    binding.txtStatusBadge.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.md_theme_light_primaryContainer)
                    )
                    binding.txtStatusBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.md_theme_light_onPrimaryContainer)
                    )

                    binding.layoutProgress.visibility = View.VISIBLE
                    binding.progressIndicator.isIndeterminate = false
                    binding.progressIndicator.progress = app.progress
                    binding.txtProgressPercent.text = "%${app.progress} İndiriliyor..."

                    binding.btnAction.isEnabled = false
                    binding.btnAction.text = "İndiriliyor..."
                    binding.btnAction.setIconResource(R.drawable.ic_download)
                }
                InstallStatus.INSTALLING -> {
                    binding.txtStatusBadge.text = "Kuruluyor"
                    binding.txtStatusBadge.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.md_theme_light_primaryContainer)
                    )
                    binding.txtStatusBadge.setTextColor(
                        ContextCompat.getColor(context, R.color.md_theme_light_onPrimaryContainer)
                    )

                    binding.layoutProgress.visibility = View.VISIBLE
                    binding.progressIndicator.isIndeterminate = true
                    binding.txtProgressPercent.text = "Paket sisteme kuruluyor..."

                    binding.btnAction.isEnabled = false
                    binding.btnAction.text = "Kuruluyor..."
                    binding.btnAction.setIconResource(R.drawable.ic_download)
                }
            }
        }
    }

    companion object {
        const val PAYLOAD_PROGRESS = "PAYLOAD_PROGRESS"
    }
}

class AppDiffCallback : DiffUtil.ItemCallback<AppModel>() {
    override fun areItemsTheSame(oldItem: AppModel, newItem: AppModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AppModel, newItem: AppModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: AppModel, newItem: AppModel): Any? {
        return if (oldItem.progress != newItem.progress || oldItem.status != newItem.status) {
            AppAdapter.PAYLOAD_PROGRESS
        } else {
            null
        }
    }
}
