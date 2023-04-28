package com.bogsnebes.weareknow.accessibility.action

import android.content.Context
import com.bogsnebes.weareknow.data.dto.ActionsDto
import com.bogsnebes.weareknow.data.impl.ActionImpl
import java.sql.Timestamp

object ActionSaver {
    fun save(
        options: List<String>, context: Context, appPkg: String,
        screenshotPath: String? = null,
        filter: (String) -> Boolean = { it != "" }
    ) {
        ActionImpl(context).insert(
            ActionsDto(
                appName = appPkg,
                date = Timestamp(System.currentTimeMillis()),
                action = options.filter(filter).joinToString(" "),
                screenshotPath = screenshotPath
            )
        )
    }

    fun save(actionsDto: ActionsDto, context: Context) {
        ActionImpl(context).insert(actionsDto)
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