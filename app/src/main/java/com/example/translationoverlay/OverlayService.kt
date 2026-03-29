package com.example.translationoverlay

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
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
        val key = "${bounds.left}-${bounds.top}-${bounds.right}-${bounds.bottom}"
        
        // Remove existing overlay for this position
        overlays[key]?.let {
            windowManager.removeView(it)
            overlays.remove(key)
        }

        // Create new overlay
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

        windowManager.addView(textView, params)
        overlays[key] = textView
    }

    fun clearAllOverlays() {
        overlays.values.forEach {
            windowManager.removeView(it)
        }
        overlays.clear()
    }
}
