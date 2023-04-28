package com.bogsnebes.weareknow.ui.main_menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bogsnebes.weareknow.data.TestObjects

class MainMenuViewModel(application: Application) : AndroidViewModel(application) {
    private var _iconsScreenState: MutableLiveData<IconsScreenState> = MutableLiveData()
    val iconsScreenState: LiveData<IconsScreenState>
        get() = _iconsScreenState

    fun getItems() {
        _iconsScreenState.value = IconsScreenState.Result(TestObjects.icons)
    }
}