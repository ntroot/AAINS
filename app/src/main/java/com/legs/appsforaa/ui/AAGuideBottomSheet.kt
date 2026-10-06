package com.legs.appsforaa.ui

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.legs.appsforaa.databinding.BottomSheetAaGuideBinding

class AAGuideBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAaGuideBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAaGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCloseGuide.setOnClickListener {
            dismiss()
        }

        binding.btnOpenAASettings.setOnClickListener {
            openAndroidAutoSettings()
        }
    }

    private fun openAndroidAutoSettings() {
        val context = requireContext()
        val aaPackage = "com.google.android.projection.gearhead"

        // 1. Doğrudan Android Auto ayarlar aktivitesi
        try {
            val intent = Intent().apply {
                component = ComponentName(
                    aaPackage,
                    "com.google.android.projection.gearhead.companion.settings.DefaultSettingsActivity"
                )
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            return
        } catch (_: Exception) {
        }

        // 2. Google Play Services Car Settings Intent
        try {
            val intent = Intent("com.google.android.gms.car.SETTINGS").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            return
        } catch (_: Exception) {
        }

        // 3. Android Auto Uygulama Bilgisi Sayfası Fallback
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$aaPackage")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Android Auto ayarlarına ulaşılamadı. Cihazınızda Android Auto yüklü mü?", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AAGuideBottomSheet"
        fun newInstance() = AAGuideBottomSheet()
    }
}
