package com.bogsnebes.weareknow.data.impl

import android.content.Context
import com.bogsnebes.weareknow.data.AppDatabase
import com.bogsnebes.weareknow.data.dao.ActionsDao
import com.bogsnebes.weareknow.data.dto.ActionsDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActionImpl(private val context: Context) {
    private val actionDao: ActionsDao = AppDatabase.getDatabase(context).actionsDao()

    fun insert(action: ActionsDto) {
        CoroutineScope(Dispatchers.IO).launch {
            actionDao.insertAction(action)
        }
    }
}