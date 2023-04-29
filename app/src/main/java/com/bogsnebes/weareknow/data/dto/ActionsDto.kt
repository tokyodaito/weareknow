package com.bogsnebes.weareknow.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "actions")
data class ActionsDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "app_name")
    var appName: String,
    var date: Timestamp,
    var action: String,
    @ColumnInfo(name = "screenshot_path")
    var screenshotPath: String? = null


) {
    override fun toString(): String {
        return "$date: $action ${if (screenshotPath != null) "with screen" else ""}"
    }
}
