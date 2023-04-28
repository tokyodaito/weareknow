package com.bogsnebes.weareknow.ui.main_menu

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bogsnebes.weareknow.data.impl.ActionsImpl
import com.bogsnebes.weareknow.ui.actions.actions_adapter.Action
import com.bogsnebes.weareknow.ui.main_menu.icon_adapter.Icon
import kotlinx.coroutines.launch

class MainMenuViewModel(application: Application) : AndroidViewModel(application) {
    private val actionsImpl: ActionsImpl = ActionsImpl(application)

    private var _iconsScreenState: MutableLiveData<IconsScreenState> = MutableLiveData()
    val iconsScreenState: LiveData<IconsScreenState>
        get() = _iconsScreenState

    private fun getAppIconByPackageName(context: Context, packageName: String): Drawable? {
        return try {
            val packageManager = context.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationIcon(appInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            // Handle the exception if the package name is not found
            null
        }
    }

    private fun getAppLabel(context: Context, packageName: String): String {
        val packageManager = context.packageManager
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }
    }

    fun getItems(context: Context) {
        viewModelScope.launch {
            val actions = actionsImpl.getAllActions()
            val icons = actions.groupBy { it.appName }
                .map { (appName, actionsList) ->
                    val actionList = actionsList.map { actionDto ->
                        Action(
                            id = actionDto.id.toInt(),
                            action = actionDto.action,
                            date = actionDto.date.toString(),
                            imageResource = actionDto.screenshotPath
                        )
                    }
                    Icon(
                        iconImage = null,
                        nameApp = getAppLabel(context, appName),
                        actions = actionList
                    )
                }
            _iconsScreenState.value = IconsScreenState.Result(icons)
        }
    }

}