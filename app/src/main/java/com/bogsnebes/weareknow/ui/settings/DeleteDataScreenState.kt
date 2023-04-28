package com.bogsnebes.weareknow.ui.settings

import android.util.Log

sealed class DeleteDataScreenState {
    object Result : DeleteDataScreenState()

    object Loading : DeleteDataScreenState()

    class Error(error: Throwable) : DeleteDataScreenState() {
        init {
            Log.e(TAG, "Error:", error)
        }
    }

    companion object {
        private const val TAG = "DeleteDataScreenState"
    }
}