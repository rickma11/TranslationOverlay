package com.example.translationoverlay

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnEnableAccessibility = findViewById<Button>(R.id.btnEnableAccessibility)
        val btnEnableOverlay = findViewById<Button>(R.id.btnEnableOverlay)
        val btnStartService = findViewById<Button>(R.id.btnStartService)
        val btnStopService = findViewById<Button>(R.id.btnStopService)

        btnEnableAccessibility.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

        btnEnableOverlay.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "悬浮窗权限已开启", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnStartService.setOnClickListener {
            val intent = Intent(this, TranslationAccessibilityService::class.java)
            startService(intent)
            Toast.makeText(this, "翻译服务已启动", Toast.LENGTH_SHORT).show()
        }

        btnStopService.setOnClickListener {
            val intent = Intent(this, TranslationAccessibilityService::class.java)
            stopService(intent)
            Toast.makeText(this, "翻译服务已停止", Toast.LENGTH_SHORT).show()
        }
    }
}
