package com.bogsnebes.weareknow.accessibility.utils

import SimpleScheduler
import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.*
import com.bogsnebes.weareknow.accessibility.action.ActionConst.TIMEUNIT_SEC
import com.bogsnebes.weareknow.accessibility.action.ActionObject.ELEM
import com.bogsnebes.weareknow.accessibility.action.ActionObject.ON
import com.bogsnebes.weareknow.accessibility.action.ActionSaver
import com.bogsnebes.weareknow.accessibility.action.ActionSubject.SYSTEM
import com.bogsnebes.weareknow.accessibility.action.ActionSubject.USER
import com.bogsnebes.weareknow.accessibility.action.ActionSubject.VIEW
import com.bogsnebes.weareknow.accessibility.action.ActionType.CLICK
import com.bogsnebes.weareknow.accessibility.action.ActionType.MOVE
import com.bogsnebes.weareknow.accessibility.action.ActionType.OPEN
import com.bogsnebes.weareknow.accessibility.action.ActionType.SCROLL
import com.bogsnebes.weareknow.accessibility.action.EventTypes.ANNOUNCEMENT
import com.bogsnebes.weareknow.accessibility.action.EventTypes.ASSIST_READING_CONTEXT
import com.bogsnebes.weareknow.accessibility.action.EventTypes.GESTURE_DETECTION_END
import com.bogsnebes.weareknow.accessibility.action.EventTypes.GESTURE_DETECTION_START
import com.bogsnebes.weareknow.accessibility.action.EventTypes.NOTIFICATION_STATE_CHANGED
import com.bogsnebes.weareknow.accessibility.action.EventTypes.SPEECH_STATE_CHANGE
import com.bogsnebes.weareknow.accessibility.action.EventTypes.TOUCH_EXPLORATION_GESTURE_END
import com.bogsnebes.weareknow.accessibility.action.EventTypes.TOUCH_EXPLORATION_GESTURE_START
import com.bogsnebes.weareknow.accessibility.action.EventTypes.TOUCH_INTERACTION_END
import com.bogsnebes.weareknow.accessibility.action.EventTypes.TOUCH_INTERACTION_START
import com.bogsnebes.weareknow.accessibility.action.EventTypes.VIEW_ACCESSIBILITY_FOCUSED
import com.bogsnebes.weareknow.accessibility.action.EventTypes.VIEW_ACCESSIBILITY_FOCUS_CLEARED
import com.bogsnebes.weareknow.accessibility.action.EventTypes.VIEW_CONTEXT_CLICKED
import com.bogsnebes.weareknow.accessibility.action.EventTypes.VIEW_FOCUSED
import com.bogsnebes.weareknow.accessibility.action.EventTypes.VIEW_HOVER_ENTER
import com.bogsnebes.weareknow.accessibility.action.EventTypes.VIEW_HOVER_EXIT
import com.bogsnebes.weareknow.accessibility.action.EventTypes.VIEW_TEXT_CHANGED
import com.bogsnebes.weareknow.accessibility.action.EventTypes.VIEW_TEXT_SELECTION_CHANGED
import com.bogsnebes.weareknow.accessibility.action.EventTypes.VIEW_TEXT_TRAVERSED
import com.bogsnebes.weareknow.accessibility.action.EventTypes.WINDOWS_CHANGED
import com.bogsnebes.weareknow.accessibility.utils.AcsUtils.mkRect
import com.bogsnebes.weareknow.accessibility.utils.AcsUtils.takeScreenshot
import com.bogsnebes.weareknow.common.StatusSaver
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

object EventProcessor {
    private const val SCROLL_DELAY = 700L
    private const val SCHEDULER_DELAY = 700L
    private const val SYSTEM_GARBAGE = 550L

    private val scheduler = SimpleScheduler()

    private var durationScroll = 0L
    private var firstScrollTime = 0L
    private var lastScrollTime = 0L

    fun process(event: AccessibilityEvent, service: AccessibilityService, context: Context) {
        runBlocking {
            launch {
                SimpleWorker.executeInBackground {
                    if (!StatusSaver.IS_ON) {
                        when (event.eventType) {
                            TYPE_VIEW_CLICKED -> processClick(event, context, service)
                            TYPE_VIEW_LONG_CLICKED -> processLongClick(event, context, service)
                            TYPE_VIEW_SCROLLED -> launch { processScroll(event, context) }
                            TYPE_VIEW_SELECTED -> processSelecting(event, context)
                            WINDOWS_CHANGE_ACTIVE, WINDOWS_CHANGE_BOUNDS -> processWindow(event, service, context)
                            TYPE_ANNOUNCEMENT -> processAnnouncement(event, context)
                            TYPE_ASSIST_READING_CONTEXT -> processAssistReadingContext(event, context)
                            TYPE_GESTURE_DETECTION_END -> processGestureDetectionEnd(event, context)
                            TYPE_GESTURE_DETECTION_START -> processGestureDetectionStart(event, context)
                            TYPE_NOTIFICATION_STATE_CHANGED -> processNotificationStateChanged(event, context)
                            TYPE_SPEECH_STATE_CHANGE -> processSpeechStateChange(event, context)
                            TYPE_TOUCH_EXPLORATION_GESTURE_END -> processTouchExplorationGestureEnd(event, context)
                            TYPE_TOUCH_EXPLORATION_GESTURE_START -> processTouchExplorationGestureStart(event, context)
                            TYPE_TOUCH_INTERACTION_END -> processTouchInteractionEnd(event, context)
                            TYPE_TOUCH_INTERACTION_START -> processTouchInteractionStart(event, context)
                            TYPE_VIEW_ACCESSIBILITY_FOCUSED -> processViewAccessibilityFocused(event, context)
                            TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> processViewAccessibilityFocusCleared(event, context)
                            TYPE_VIEW_CONTEXT_CLICKED -> processViewContextClicked(event, context)
                            TYPE_VIEW_FOCUSED -> processViewFocused(event, context)
                            TYPE_VIEW_HOVER_ENTER -> processViewHoverEnter(event, context)
                            TYPE_VIEW_HOVER_EXIT -> processViewHoverExit(event, context)
                            TYPE_VIEW_TEXT_CHANGED -> processViewTextChanged(event, context)
                            TYPE_VIEW_TEXT_SELECTION_CHANGED -> processViewTextSelectionChanged(event, context)
                            TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY -> processViewTextTraversed(event, context)
                            else -> {
                                // Handle unknown or unexpected event types here if needed
                            }
                        }
                    }
                }
            }
        }
    }

    private fun processAnnouncement(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(SYSTEM, ANNOUNCEMENT), context)
    }

    private fun processAssistReadingContext(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(SYSTEM, ASSIST_READING_CONTEXT), context)
    }

    private fun processGestureDetectionEnd(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(SYSTEM, GESTURE_DETECTION_END), context)
    }

    private fun processGestureDetectionStart(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(SYSTEM, GESTURE_DETECTION_START), context)
    }

    private fun processNotificationStateChanged(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(SYSTEM, NOTIFICATION_STATE_CHANGED), context)
    }

    private fun processSpeechStateChange(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(SYSTEM, SPEECH_STATE_CHANGE), context)
    }

    private fun processTouchExplorationGestureEnd(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, TOUCH_EXPLORATION_GESTURE_END), context)
    }

    private fun processTouchExplorationGestureStart(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, TOUCH_EXPLORATION_GESTURE_START), context)
    }

    private fun processTouchInteractionEnd(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, TOUCH_INTERACTION_END), context)
    }

    private fun processTouchInteractionStart(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, TOUCH_INTERACTION_START), context)
    }

    private fun processViewAccessibilityFocused(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, VIEW_ACCESSIBILITY_FOCUSED), context)
    }

    private fun processViewAccessibilityFocusCleared(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, VIEW_ACCESSIBILITY_FOCUS_CLEARED), context)
    }

    private fun processViewContextClicked(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, VIEW_CONTEXT_CLICKED), context)
    }

    private fun processViewFocused(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, VIEW_FOCUSED), context)
    }

    private fun processViewHoverEnter(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, VIEW_HOVER_ENTER), context)
    }

    private fun processViewHoverExit(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, VIEW_HOVER_EXIT), context)
    }

    private fun processViewTextChanged(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, VIEW_TEXT_CHANGED), context)
    }

    private fun processViewTextSelectionChanged(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, VIEW_TEXT_SELECTION_CHANGED), context)
    }

    private fun processViewTextTraversed(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, VIEW_TEXT_TRAVERSED), context)
    }

    private fun processWindowsChanged(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(SYSTEM, WINDOWS_CHANGED), context)
    }


    private fun processAbstractNode(
        event: AccessibilityEvent,
        eventOptions: List<String>,
        context: Context,
        screen: String? = null
    ) {
        if (event.contentDescription != null) return processAbstractEvent(
            event,
            eventOptions,
            context, screen
        )
        val node = event.source ?: return

        try {
            val text = NodeUtil.getUsefulTextFromHierarchy(node)
            if (text == "") return
            val nodeType = NodeUtil.getNodeType(node)
            ActionSaver.save(
                eventOptions + listOf(nodeType, text),
                context, event.packageName.toString(), screen
            )
        } finally {
            node.recycle()
        }
    }

    private fun processAbstractEvent(
        event: AccessibilityEvent,
        options: List<String>,
        context: Context,
        screen: String? = null
    ) {
        val text = NodeUtil.getUsefulTextFromEvent(event)
        if (text == "") return
        val nodeType = NodeUtil.getType(event.className.toString())
        ActionSaver.save(
            options + listOf(nodeType, text),
            context, event.packageName.toString(), screen
        )
    }

    @SuppressLint("NewApi")
    private fun processClick(
        event: AccessibilityEvent,
        context: Context,
        service: AccessibilityService
    ) {
        takeScreenshot(
            service, context, ScreenshotCallBack(
                context, 85, mkRect(event), ActionSaver.build(
                    listOf(USER, CLICK, ON),
                    event.packageName.toString(), null
                )
            )
        )
    }

    @SuppressLint("NewApi")
    private fun processLongClick(
        event: AccessibilityEvent,
        context: Context,
        service: AccessibilityService
    ) {
        takeScreenshot(
            service, context, ScreenshotCallBack(
                context, 85, mkRect(event), ActionSaver.build(
                    listOf(USER, CLICK, ON),
                    event.packageName.toString(), null
                )
            )
        )
    }

    private fun processSelecting(event: AccessibilityEvent, context: Context) {
        processAbstractNode(event, listOf(USER, MOVE, ELEM), context)
    }

    @SuppressLint("NewApi")
    private fun processWindow(
        event: AccessibilityEvent, service: AccessibilityService, context: Context
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            AcsUtils.takeScreenshot(
                service, context, ScreenshotCallBack(
                    context, 85,
                    null,
                    ActionSaver.build(
                        listOf(
                            SYSTEM, OPEN,
                            Util.humanizePkg(event.packageName), event.text.toString()
                        ).filter { it != "" && it != "[]" },
                        event.packageName.toString()
                    )
                )
            )
        }
    }

    private fun processScroll(event: AccessibilityEvent, context: Context) {
        if (firstScrollTime == 0L && durationScroll == 0L) {
            firstScrollTime = SystemClock.elapsedRealtime()
            lastScrollTime = SystemClock.elapsedRealtime()
        } else {
            lastScrollTime = SystemClock.elapsedRealtime()
            durationScroll = lastScrollTime - firstScrollTime
            scheduler.schedule("scroll", SCHEDULER_DELAY) {
                val delay = SystemClock.elapsedRealtime() - lastScrollTime
                if (delay > SCROLL_DELAY) {
                    if (durationScroll > SYSTEM_GARBAGE) {
                        val dur = TimeUnit.MILLISECONDS.toSeconds(durationScroll).toString()
                        event.packageName?.toString()?.let {
                            ActionSaver.save(
                                listOf(VIEW, SCROLL, dur, TIMEUNIT_SEC),
                                context, it
                            )
                        }
                    }
                    durationScroll = 0
                    lastScrollTime = 0
                    firstScrollTime = 0
                }
            }
        }
    }
}
