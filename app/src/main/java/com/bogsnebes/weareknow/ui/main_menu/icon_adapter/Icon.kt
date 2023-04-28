package com.bogsnebes.weareknow.ui.main_menu.icon_adapter

import com.bogsnebes.weareknow.ui.actions.actions_adapter.Action

data class Icon(
    var iconImage: Int? = null,
    var nameApp: String,
    var actions: List<Action>
)
