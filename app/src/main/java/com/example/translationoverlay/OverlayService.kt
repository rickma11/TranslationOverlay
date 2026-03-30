package com.example.translationoverlay

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.graphics.Typeface

class OverlayService(private val context: Context) {
    private val overlays = mutableMapOf<String, View>()
    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun showTranslation(bounds: android.graphics.Rect, text: String) {
        Log.d("Translation", "showTranslation: 显示翻译: $text, 位置: ${bounds.left},${bounds.top},${bounds.right},${bounds.bottom}")
        val key = "${bounds.left}-${bounds.top}-${bounds.right}-${bounds.bottom}"
        
        // Remove existing overlay for this position
        overlays[key]?.let {
            Log.d("Translation", "showTranslation: 移除已存在的覆盖层")
            windowManager.removeView(it)
            overlays.remove(key)
        }

        // Create new overlay
        Log.d("Translation", "showTranslation: 创建新的覆盖层")
        val textView = TextView(context).apply {
            this.text = text
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.WHITE)
            setPadding(8, 4, 8, 4)
            gravity = Gravity.CENTER
            setTypeface(null, Typeface.BOLD)
        }

        val params = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            format = PixelFormat.TRANSLUCENT
            width = bounds.width()
            height = bounds.height()
            x = bounds.left
            y = bounds.top
        }

        try {
            windowManager.addView(textView, params)
            overlays[key] = textView
            Log.d("Translation", "showTranslation: 覆盖层添加成功")
        } catch (e: Exception) {
            Log.e("Translation", "showTranslation: 添加覆盖层失败: ${e.message}")
        }
    }

    fun clearAllOverlays() {
        Log.d("Translation", "clearAllOverlays: 清除所有覆盖层")
        overlays.values.forEach {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                Log.e("Translation", "clearAllOverlays: 移除覆盖层失败: ${e.message}")
            }
        }
        overlays.clear()
        Log.d("Translation", "clearAllOverlays: 所有覆盖层已清除")
    }
}
