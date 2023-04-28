package com.bogsnebes.weareknow.data

import com.bogsnebes.weareknow.data.dto.ActionsDto
import java.sql.Timestamp

object TestObjects {
    val actions = listOf<ActionsDto>(
        ActionsDto(
            0,
            "org.telegram.messenger",
            Timestamp(System.currentTimeMillis()),
            "Пользователь нажал на элемент \"Влад. Получено в 17:23. Привет\""
        ),
        ActionsDto(
            1,
            "org.telegram.messenger",
            Timestamp(System.currentTimeMillis()),
            "Пользователь нажалGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHIGACHI на элемент \"Мария. Получено в 17:25. Как дела?\""
        ),
        ActionsDto(
            2,
            "org.telegram.messenger",
            Timestamp(System.currentTimeMillis()),
            "Пользователь нажал на элемент \"Андрей. Получено в 17:27. Что нового?\""
        ),
        ActionsDto(
            3,
            "org.telegram.messenger",
            Timestamp(System.currentTimeMillis()),
            "Пользователь нажал на элемент \"Елена. Получено в 17:29. Где встречаемся?\""
        ),
        ActionsDto(
            4,
            "org.telegram.messenger",
            Timestamp(System.currentTimeMillis()),
            "Пользователь нажал на элемент \"Игорь. Получено в 17:32. Завтра на работе обсудим?\""
        )
    )
}