package com.bogsnebes.weareknow.accessibility.utils

import SimpleScheduler
import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.*
import com.bogsnebes.weareknow.accessibility.action.ActionBuilder
import com.bogsnebes.weareknow.accessibility.action.ActionConst.TIME_UNIT
import com.bogsnebes.weareknow.accessibility.action.ActionSaver
import com.bogsnebes.weareknow.accessibility.action.ActionSubject.SYSTEM
import com.bogsnebes.weareknow.accessibility.action.ActionSubject.USER
import com.bogsnebes.weareknow.accessibility.action.ActionSubject.VIEW
import com.bogsnebes.weareknow.accessibility.action.ActionType.CLICK
import com.bogsnebes.weareknow.accessibility.action.ActionType.OPEN
import com.bogsnebes.weareknow.accessibility.action.ActionType.SCROLL
import java.util.concurrent.TimeUnit

object EventProcessor {
    private const val SCROLL_DELAY = 700L
    private const val SCHEDULER_DELAY = 700L

    private var durationScroll = 0L
    private var firstScrollTime = 0L
    private var lastScrollTime = 0L

    fun process(event: AccessibilityEvent, service: AccessibilityService, context: Context) {
        when (event.eventType) {
            TYPE_VIEW_CLICKED, TYPE_VIEW_LONG_CLICKED -> processClick(event)
            WINDOWS_CHANGE_ACTIVE -> processWindow(event, service, context)
            TYPE_VIEW_SCROLLED -> processScroll(event)
        }
        processScroll(event)
    }

    private fun processEvent(event: AccessibilityEvent) {
        val text = NodeUtil.getUsefulTextFromEvent(event)
        if (text == "") return
        val nodeType = NodeUtil.getType(event.className.toString())
        ActionSaver.save(ActionBuilder.createAction(
            listOf(USER, CLICK, nodeType, text).filter { it != "" }
        ))
    }

    private fun processClick(event: AccessibilityEvent) {
        val node = event.source ?: return processEvent(event)

        try {
            val text = NodeUtil.getUsefulTextFromHierarchy(node)
            if (text == "") return
            val nodeType = NodeUtil.getNodeType(node)
            ActionSaver.save(ActionBuilder.createAction(
                listOf(USER, CLICK, nodeType, text).filter { it != "" }
            ))
        } finally {
            node.recycle()
        }
    }

    @SuppressLint("NewApi")
    private fun processWindow(
        event: AccessibilityEvent, service: AccessibilityService, context: Context
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            AcsUtils.takeScreenshot(service, context, ScreenshotCallBack(context, 85))
        }
        ActionSaver.save(
            ActionBuilder.createAction(
                listOf(SYSTEM, OPEN, event.packageName.toString())
            )
        )
    }

    private fun processScroll(event: AccessibilityEvent) {
        if (durationScroll == 0L) {
            durationScroll = 1L
            firstScrollTime = SystemClock.elapsedRealtime()
            lastScrollTime = SystemClock.elapsedRealtime()
        } else {
            lastScrollTime = SystemClock.elapsedRealtime()
            durationScroll += lastScrollTime - firstScrollTime
        }
        SimpleScheduler().schedule("scroll", SCHEDULER_DELAY, 0) {
            val delay = SystemClock.elapsedRealtime() - lastScrollTime
            if (delay > SCROLL_DELAY) {
                if (durationScroll > 10)
                    ActionSaver.save(ActionBuilder.createAction(
                        listOf(
                            VIEW, SCROLL,
                            TimeUnit.MILLISECONDS.toSeconds(durationScroll).toString(),
                            TIME_UNIT
                        ).filter { it != "" }
                    ))
                durationScroll = 0
                lastScrollTime = 0
                firstScrollTime = 0
            }
        }
    }
}
