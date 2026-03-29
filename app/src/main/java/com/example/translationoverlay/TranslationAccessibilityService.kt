package com.example.translationoverlay

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.res.AssetManager
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import org.json.JSONObject

class TranslationAccessibilityService : AccessibilityService() {
    private lateinit var overlayService: OverlayService
    private val dictionary = mutableMapOf<String, String>()

    override fun onCreate() {
        super.onCreate()
        overlayService = OverlayService(this)
        loadDictionary()
    }

    private fun loadDictionary() {
        try {
            val inputStream = assets.open("dict.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                dictionary[key] = jsonObject.getString(key)
            }
        } catch (e: Exception) {
            Log.e("TranslationService", "Error loading dictionary: ${e.message}")
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val rootNode = rootInActiveWindow ?: return
        processNode(rootNode)
    }

    private fun processNode(node: AccessibilityNodeInfo) {
        if (node.text != null) {
            val text = node.text.toString()
            if (dictionary.containsKey(text)) {
                val bounds = Rect()
                node.getBoundsInScreen(bounds)
                overlayService.showTranslation(bounds, dictionary[text] ?: text)
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                processNode(child)
                child.recycle()
            }
        }
    }

    override fun onInterrupt() {}
}
