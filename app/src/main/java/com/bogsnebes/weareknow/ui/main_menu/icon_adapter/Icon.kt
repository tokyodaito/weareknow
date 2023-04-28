package com.bogsnebes.weareknow.ui.main_menu.icon_adapter

import android.graphics.drawable.Drawable
import com.bogsnebes.weareknow.ui.actions.actions_adapter.Action

data class Icon(
    var iconImage: Drawable? = null,
    var nameApp: String,
    var actions: List<Action>
)
