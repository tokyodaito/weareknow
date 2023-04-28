package com.bogsnebes.weareknow.accessibility

import android.accessibilityservice.AccessibilityGestureEvent
import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.bogsnebes.weareknow.accessibility.utils.EventProcessor
import com.bogsnebes.weareknow.gestures.GestureProcessor

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
//        println("Event: ${event.eventType}")
        EventProcessor.process(event, this, applicationContext)
    }

    override fun onGesture(gestureEvent: AccessibilityGestureEvent): Boolean {
        println("Event: ${gestureEvent}")
        GestureProcessor.process(gestureEvent, this, applicationContext)
        return true
    }

    override fun onInterrupt() {
        // Обработка прерываний
    }
}