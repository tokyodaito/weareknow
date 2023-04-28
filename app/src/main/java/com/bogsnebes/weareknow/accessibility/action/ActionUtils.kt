package com.bogsnebes.weareknow.accessibility.action

import java.sql.Timestamp

object ActionConst{
    const val TIMEUNIT_SEC = "сек."
}

object ActionSubject {
    const val SYSTEM = "Система"
    const val APP = "Приложение"
    const val USER = "Пользователь"
    const val VIEW = "Окно"
}

object ActionType {
    const val CLICK = "нажал"
    const val DRAG = "перенес"
    const val OPEN = "открыла"
    const val SCROLL = "скролилось:"
    const val MOVE = "переместил"
}

object ActionObject {
    const val ON = "на"
    const val ELEM = "элемент"
    const val BUTTON = "кнопку"
    const val VIEW = "окно"
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
        return Action(options.joinToString(" "), Timestamp(System.currentTimeMillis()))
    }
}