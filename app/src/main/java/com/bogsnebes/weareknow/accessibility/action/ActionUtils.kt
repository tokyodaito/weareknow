package com.bogsnebes.weareknow.accessibility.action

import java.sql.Timestamp

object ActionSubject {
    const val SYSTEM = "система"
    const val APP = "приложение"
    const val USER = "пользователь"
}

object ActionType {
    const val CLICK = "нажал"
    const val CLICK_LONG = "зажал"
    const val DRAG = "перенес"
    const val OPEN = "открыла"
}

object ActionObject {
    const val ELEM = "на элемент"
    const val BUTTON = "кнопку"
    const val VIEW = "на окно"
}

data class Action(
    val action: String,
    val timestamp: Timestamp
) {
    override fun toString(): String {
        return "$timestamp: $action"
    }
}

object ActionBuilder {
    fun createAction(options: List<String>): Action {
        options[0].replaceFirstChar { it.titlecaseChar() }

        return Action(options.joinToString(" "), Timestamp(System.currentTimeMillis()))
    }
}