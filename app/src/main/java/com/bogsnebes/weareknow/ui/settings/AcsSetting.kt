package com.bogsnebes.weareknow.ui.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.bogsnebes.weareknow.common.CommonValues

object AcsSetting {
    fun showAcsSetting(context: Context) {
//        if (!CommonValues.IS_SPEC_ON) return
//        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        }
//        context.startActivity(intent)
    }

    fun openDeveloperSettings(context: Context) {
        Toast.makeText(context, "Ввод -> Показывать нажатия", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}