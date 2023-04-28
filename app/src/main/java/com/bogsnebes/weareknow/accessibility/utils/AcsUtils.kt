package com.bogsnebes.weareknow.accessibility.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.ScreenshotResult
import android.accessibilityservice.AccessibilityService.TakeScreenshotCallback
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import com.bogsnebes.weareknow.accessibility.action.ActionSaver
import com.bogsnebes.weareknow.data.dto.ActionsDto
import java.io.File
import java.io.FileOutputStream

lateinit var currentView: Bitmap
var intervalMillis = 300L
var lastScreenTime = 0L

fun cropBitmap(bitmap: Bitmap, rect: Rect): Bitmap {
    val mutableBitmap = if (!bitmap.isMutable) {
        bitmap.copy(Bitmap.Config.ARGB_8888, true)
    } else {
        bitmap
    }
    return Bitmap.createBitmap(mutableBitmap, rect.left, rect.top, rect.width(), rect.height())
}

@RequiresApi(Build.VERSION_CODES.R)
class ScreenshotCallBack(
    private val context: Context,
    private val compressionQuality: Int,
    private val rect: Rect? = null,
    private val actionsDto: ActionsDto
) : TakeScreenshotCallback {
    override fun onSuccess(p0: ScreenshotResult) {
        try {
            lastScreenTime = SystemClock.elapsedRealtime()
            currentView = Bitmap.wrapHardwareBuffer(p0.hardwareBuffer, p0.colorSpace) ?: return
            rect?.let {
                currentView = cropBitmap(currentView, rect)
            }
            val folder = File(context.applicationContext.externalCacheDir, "images/now")
            folder.mkdirs()

            val file = File(folder, SystemClock.elapsedRealtime().toString() + ".jpeg")

            actionsDto.screenshotPath = file.absolutePath
            ActionSaver.save(actionsDto, context)
            FileOutputStream(file).use {
                currentView.compress(Bitmap.CompressFormat.JPEG, compressionQuality, it)
            }
            Log.e("ScreenshotCallBack", "Скриншот успешно сохранен")
        } catch (ex: Exception) {
            Log.e("ScreenshotCallBack", "Не возможно получить скриншот")
        }
    }

    override fun onFailure(p0: Int) {
        Log.e("ScreenshotCallBack", "Не возможно получить скриншот")
    }
}

object AcsUtils {
    @SuppressLint("NewApi")
    fun takeScreenshot(
        service: AccessibilityService, context: Context,
        callBack: ScreenshotCallBack
    ) {
        if (SystemClock.elapsedRealtime() - lastScreenTime > intervalMillis)
            service.takeScreenshot(0, context.mainExecutor, callBack)
    }

    fun takePostScreenshot(
        context: Context,
        rect: Rect
    ): String {
        try {
            val bitmap = cropBitmap(currentView, rect)
            val folder = File(context.applicationContext.externalCacheDir, "images/post")
            folder.mkdirs()

            val file = File(folder, SystemClock.elapsedRealtime().toString() + ".jpeg")

            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it)
            }
            Log.e("ScreenshotCallBack", "Скриншот успешно сохранен")
            return file.absolutePath
        } catch (ex: Exception) {
            Log.e("ScreenshotCallBack", "Не возможно получить скриншот", ex)
            return ""
        }
    }

    fun mkRect(event: AccessibilityEvent): Rect {
        val rect: Rect = Rect()
        event.source?.getBoundsInScreen(rect)
        return rect
    }
}