package com.bogsnebes.weareknow.ui.actions.actions_adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Action(
    val id: Int,
    val action: String,
    val date: String,
    val imageResource: String?
) : Parcelable
