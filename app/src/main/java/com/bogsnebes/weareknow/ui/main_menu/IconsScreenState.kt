package com.bogsnebes.weareknow.ui.main_menu

import android.util.Log
import com.bogsnebes.weareknow.ui.main_menu.icon_adapter.Icon

sealed class IconsScreenState {
    class Result(val items: List<Icon>) : IconsScreenState()

    object Loading : IconsScreenState()

    class Error(error: Throwable) : IconsScreenState() {
        init {
            Log.e(TAG, "Error:", error)
        }
    }

    companion object {
        private const val TAG = "IconsScreenState"
    }
}