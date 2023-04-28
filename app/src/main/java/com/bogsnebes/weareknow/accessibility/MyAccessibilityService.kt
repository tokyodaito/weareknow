package com.bogsnebes.weareknow.accessibility

import android.accessibilityservice.AccessibilityGestureEvent
import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.bogsnebes.weareknow.accessibility.utils.EventProcessor

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
//        println("Event: ${event.eventType}")
        EventProcessor.process(event, this, applicationContext)
    }

    override fun onGesture(gestureEvent: AccessibilityGestureEvent): Boolean {
        println("Event: $gestureEvent")
        return true
    }

    override fun onInterrupt() {
        // Обработка прерываний
    }
}