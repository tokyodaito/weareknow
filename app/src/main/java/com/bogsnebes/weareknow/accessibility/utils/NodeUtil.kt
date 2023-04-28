package com.bogsnebes.weareknow.accessibility.utils

import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.bogsnebes.weareknow.accessibility.action.ActionObject.BUTTON
import com.bogsnebes.weareknow.accessibility.action.ActionObject.ELEM
import com.bogsnebes.weareknow.accessibility.action.ActionObject.ON_ELEM
import com.bogsnebes.weareknow.accessibility.action.ActionObject.VIEW
import com.bogsnebes.weareknow.accessibility.utils.Util.surroundString

object NodeUtil {
    fun getUsefulTextFromNode(node: AccessibilityNodeInfo): String {
        if (node.text != null) return surroundString(node.text.toString(), "\"")
        if (node.contentDescription != null) return surroundString(
            node.contentDescription.toString(),
            "\""
        )
        return ""
    }

    fun getUsefulTextFromEvent(event: AccessibilityEvent): String {
        if (event.contentDescription != null) return surroundString(
            event.contentDescription.toString(), "\""
        )
        if (event.text.size > 0) return surroundString(event.text.toString(), "\"")
        return ""
    }

    fun getUsefulTextFromHierarchy(node: AccessibilityNodeInfo?): String {
        if (node == null) return ""

        val text = getUsefulTextFromNode(node)
        if (text != "") return text

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            try {
                val resultText = getUsefulTextFromNode(child)
                if (resultText != "") return resultText
            } finally {
                child.recycle()
            }
        }
        return ""
    }

    fun getType(clazz: String): String {
        return when {
            clazz.contains("Button") -> BUTTON
            clazz.contains("Check") -> BUTTON
            clazz.contains("Plane") -> VIEW
            clazz.contains("View") -> ELEM
            else -> ""
        }
    }

    fun getNodeType(node: AccessibilityNodeInfo): String {
        return getType(node.className.toString())
    }

    fun getHumanNodePosition(node: AccessibilityNodeInfo): String {
        val rect = Rect()
        node.getBoundsInScreen(rect)
        return rect.toShortString()
    }
}