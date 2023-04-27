package com.bogsnebes.weareknow.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.bogsnebes.weareknow.accessibility.utils.EventProcessor

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
//        println(event.eventType)
        EventProcessor.process(event)
    }

    override fun onInterrupt() {
        // Обработка прерываний
    }
}