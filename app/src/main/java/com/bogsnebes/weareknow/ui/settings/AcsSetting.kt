package com.bogsnebes.weareknow.ui.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.bogsnebes.weareknow.common.CommonValues

object AcsSetting {
    fun showAcsSetting(context: Context) {
        if (!CommonValues.IS_SPEC_ON) return
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}