package com.bogsnebes.weareknow.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private var _deleteDataScreenState: MutableLiveData<DeleteDataScreenState> = MutableLiveData()
    val deleteDataScreenState: LiveData<DeleteDataScreenState>
        get() = _deleteDataScreenState

    fun deleteAllData() {
        _deleteDataScreenState.value = DeleteDataScreenState.Result
    }
}