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
import com.bogsnebes.weareknow.accessibility.utils.AcsUtils.mkRect
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
                            TYPE_VIEW_CLICKED, TYPE_VIEW_LONG_CLICKED -> processClick(
                                event,
                                context
                            )
                            WINDOWS_CHANGE_ACTIVE, WINDOWS_CHANGE_BOUNDS -> processWindow(
                                event,
                                service,
                                context
                            )
                            TYPE_VIEW_SCROLLED -> processScroll(event, context)
                            TYPE_VIEW_SELECTED -> processSelecting(event, context)
                        }
                    }
                }
            }
        }
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

    private fun processClick(event: AccessibilityEvent, context: Context) {
        val screen = AcsUtils.takePostScreenshot(context, mkRect(event))
        processAbstractNode(event, listOf(USER, CLICK, ON), context, screen)
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
