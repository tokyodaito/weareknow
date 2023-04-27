package com.bogsnebes.weareknow.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        println("Clicked event from package: ${event.eventType}")
        try {
            if (event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                // Обрабатываем событие нажатия
                val packageName = event.packageName
                val sourceInfo: CharSequence =
                    if (event.source != null) event.source.toString() else "null"
                println("Clicked event from package: $packageName, source: $sourceInfo")
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun onInterrupt() {
        // Обработка прерываний
    }
}