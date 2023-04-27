package com.bogsnebes.weareknow.accessibility.utils

import android.view.inputmethod.SurroundingText

object Util {
    fun surroundString(string: String, surroundingText: String): String {
        return "$surroundingText$string$surroundingText"
    }

    fun humanizePkg(packageStr: CharSequence): String {
        return packageStr.toString().substringAfter(".")
    }
}