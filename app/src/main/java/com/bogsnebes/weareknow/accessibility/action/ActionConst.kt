package com.bogsnebes.weareknow.accessibility.action

object ActionConst {
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

object EventTypes {
    const val ANNOUNCEMENT = "объявление"
    const val ASSIST_READING_CONTEXT = "помощь в чтении контекста"
    const val GESTURE_DETECTION_END = "определение жестов окончание"
    const val GESTURE_DETECTION_START = "определение жестов начало"
    const val NOTIFICATION_STATE_CHANGED = "состояние уведомлений изменено"
    const val SPEECH_STATE_CHANGE = "состояние речи изменено"
    const val TOUCH_EXPLORATION_GESTURE_END = "исследование жестов конец"
    const val TOUCH_EXPLORATION_GESTURE_START = "исследование жестов начало"
    const val TOUCH_INTERACTION_END = "взаимодействие касанием конец"
    const val TOUCH_INTERACTION_START = "взаимодействие касанием начало"
    const val VIEW_ACCESSIBILITY_FOCUSED = "доступность фокусировки окна"
    const val VIEW_ACCESSIBILITY_FOCUS_CLEARED = "очистка фокуса доступности окна"
    const val VIEW_CONTEXT_CLICKED = "контекстное щелчком окна"
    const val VIEW_FOCUSED = "фокусировка окна"
    const val VIEW_HOVER_ENTER = "поднесение курсора над окном"
    const val VIEW_HOVER_EXIT = "убирание курсора из окна"
    const val VIEW_TEXT_CHANGED = "изменение текста окна"
    const val VIEW_TEXT_SELECTION_CHANGED = "изменение выделения текста окна"
    const val VIEW_TEXT_TRAVERSED = "перемещение по тексту окна"
    const val WINDOWS_CHANGED = "изменение окон"
}