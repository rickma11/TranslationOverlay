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
        Log.d("Translation", "onCreate: 服务创建")
        overlayService = OverlayService(this)
        loadDictionary()
    }

    private fun loadDictionary() {
        Log.d("Translation", "loadDictionary: 开始加载词典")
        try {
            val inputStream = assets.open("dict.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                dictionary[key] = jsonObject.getString(key)
            }
            Log.d("Translation", "loadDictionary: 词典加载完成，词条数量: ${dictionary.size}")
        } catch (e: Exception) {
            Log.e("Translation", "loadDictionary: 加载词典失败: ${e.message}")
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d("Translation", "onAccessibilityEvent: 接收到事件: ${event.eventType}")
        val rootNode = rootInActiveWindow ?: run {
            Log.d("Translation", "onAccessibilityEvent: 根节点为空")
            return
        }
        Log.d("Translation", "onAccessibilityEvent: 开始处理节点")
        processNode(rootNode)
    }

    private fun processNode(node: AccessibilityNodeInfo) {
        if (node.text != null) {
            val text = node.text.toString()
            Log.d("Translation", "processNode: 检测到文本: $text")
            if (dictionary.containsKey(text)) {
                val bounds = Rect()
                node.getBoundsInScreen(bounds)
                Log.d("Translation", "processNode: 找到翻译: $text -> ${dictionary[text]}")
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
