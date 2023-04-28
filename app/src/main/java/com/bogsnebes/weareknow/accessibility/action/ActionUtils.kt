package com.bogsnebes.weareknow.accessibility.action

import java.sql.Timestamp

object ActionConst{
    const val TIMEUNIT_SEC = "сек."
}

object ActionSubject {
    const val SYSTEM = "система"
    const val APP = "приложение"
    const val USER = "пользователь"
    const val VIEW = "окно"
}

object ActionType {
    const val CLICK = "нажал"
    const val DRAG = "перенес"
    const val OPEN = "открыла"
    const val SCROLL = "скролилось:"
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