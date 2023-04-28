package com.bogsnebes.weareknow.data.dao

import androidx.room.*
import com.bogsnebes.weareknow.data.dto.ActionsDto
import java.util.*

@Dao
interface ActionsDao {

    @Insert
    suspend fun insertAction(action: ActionsDto): Long

    @Update
    suspend fun updateAction(action: ActionsDto)

    @Delete
    suspend fun deleteAction(action: ActionsDto)

    @Query("SELECT * FROM actions")
    suspend fun getAllActions(): List<ActionsDto>

    @Query("SELECT * FROM actions WHERE id = :id")
    suspend fun getActionById(id: Long): ActionsDto?

    @Query("SELECT * FROM actions WHERE app_name = :appName")
    suspend fun getActionsByAppName(appName: String): List<ActionsDto>

    @Query("SELECT * FROM actions WHERE date = :date")
    suspend fun getActionsByDate(date: Date): List<ActionsDto>

    @Query("SELECT * FROM actions WHERE action = :action")
    suspend fun getActionsByAction(action: String): List<ActionsDto>

    @Query("SELECT * FROM actions WHERE app_name = :appName AND date = :date")
    suspend fun getActionsByAppNameAndDate(appName: String, date: Date): List<ActionsDto>

    @Query("SELECT * FROM actions WHERE app_name = :appName AND action = :action")
    suspend fun getActionsByAppNameAndAction(appName: String, action: String): List<ActionsDto>

    @Query("SELECT * FROM actions WHERE date = :date AND action = :action")
    suspend fun getActionsByDateAndAction(date: Date, action: String): List<ActionsDto>

    @Query("SELECT * FROM actions WHERE app_name = :appName AND date = :date AND action = :action")
    suspend fun getActionsByAppNameDateAndAction(
        appName: String,
        date: Date,
        action: String
    ): List<ActionsDto>
}