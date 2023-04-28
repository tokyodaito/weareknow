package com.bogsnebes.weareknow.accessibility.action

import android.content.Context
import com.bogsnebes.weareknow.data.dto.ActionsDto
import com.bogsnebes.weareknow.data.impl.ActionsImpl
import java.sql.Timestamp

object ActionSaver {
    fun save(
        options: List<String>, context: Context, appPkg: String,
        screenshotPath: String? = null,
        filter: (String) -> Boolean = { it != "" }
    ) {
        val actionsDto = ActionsDto(
            appName = appPkg,
            date = Timestamp(System.currentTimeMillis()),
            action = options.filter(filter).joinToString(" "),
            screenshotPath = screenshotPath
        )
        ActionsImpl(context).insert(
            actionsDto
        )
        println(actionsDto)
    }

    fun save(actionsDto: ActionsDto, context: Context) {
        ActionsImpl(context).insert(actionsDto)
        println(actionsDto)
    }

    fun build(
        options: List<String>, appPkg: String,
        screenshotPath: String? = null,
        filter: (String) -> Boolean = { it != "" }
    ): ActionsDto {
        return ActionsDto(
            appName = appPkg,
            date = Timestamp(System.currentTimeMillis()),
            action = options.filter(filter).joinToString(" "),
            screenshotPath = screenshotPath
        )
    }
}