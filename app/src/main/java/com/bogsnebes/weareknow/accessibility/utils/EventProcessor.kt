package com.bogsnebes.weareknow.accessibility.utils

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.*
import com.bogsnebes.weareknow.accessibility.action.ActionBuilder
import com.bogsnebes.weareknow.accessibility.action.ActionSaver
import com.bogsnebes.weareknow.accessibility.action.ActionSubject.SYSTEM
import com.bogsnebes.weareknow.accessibility.action.ActionSubject.USER
import com.bogsnebes.weareknow.accessibility.action.ActionType.CLICK
import com.bogsnebes.weareknow.accessibility.action.ActionType.OPEN

object EventProcessor {
    fun process(event: AccessibilityEvent) {
        when (event.eventType) {
            TYPE_VIEW_CLICKED, TYPE_VIEW_LONG_CLICKED -> processClick(event)
            WINDOWS_CHANGE_ACTIVE -> processWindow(event)
        }
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

    private fun processWindow(event: AccessibilityEvent) {
//        ToDo make screen
        ActionSaver.save(
            ActionBuilder.createAction(
                listOf(SYSTEM, OPEN, event.packageName.toString())
            )
        )
    }

}
