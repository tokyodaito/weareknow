package com.bogsnebes.weareknow.ui.settings

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private var _deleteDataScreenState: MutableLiveData<DeleteDataScreenState> = MutableLiveData()
    val deleteDataScreenState: LiveData<DeleteDataScreenState>
        get() = _deleteDataScreenState

    private val _switchState = MutableLiveData<Boolean>()
    val switchState: LiveData<Boolean>
        get() = _switchState

    private fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val service = "${context.packageName}/.accessibility.MyAccessibilityService"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return !TextUtils.isEmpty(enabledServices) && enabledServices.contains(service)
    }

    private fun isShowTapsEnabled(context: Context): Boolean {
        return try {
            val showTapsSetting = Settings.System.getInt(context.contentResolver, "show_touches")
            showTapsSetting == 1
        } catch (e: Settings.SettingNotFoundException) {
            false
        }
    }

    private fun openDeveloperSettings(context: Context, showEnableInstruction: () -> Unit) {
        showEnableInstruction()
        val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun deleteAllData() {
        _deleteDataScreenState.value = DeleteDataScreenState.Result
    }

    fun onSwitchStateChanged(
        context: Context,
        isChecked: Boolean,
        showEnableInstruction: () -> Unit
    ) {
        if (isChecked && !isShowTapsEnabled(context)) {
            openDeveloperSettings(context, showEnableInstruction)
        } else if (isChecked && !isAccessibilityServiceEnabled(context)) {
            openAccessibilitySettings(context)
        }
    }

    fun checkSwitchState(context: Context) {
        _switchState.value = (isAccessibilityServiceEnabled(context) && isShowTapsEnabled(context))
    }
}