# 悬浮翻译应用 - TranslationOverlay

## 项目介绍

这是一个基于无障碍服务的安卓悬浮翻译应用，可以在不修改原应用的情况下，通过悬浮窗覆盖的方式为英文应用提供中文翻译。

## 技术特点

- **无需修改原应用**：通过悬浮窗覆盖实现翻译，不影响原应用
- **离线翻译**：内置词典，无需联网
- **流畅体验**：直接替换文本，无延迟
- **无需Root**：使用系统官方API，无需Root权限
- **可扩展性**：可以通过更新词典文件来增加翻译词条

## 项目结构

```
TranslationOverlay/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/translationoverlay/
│   │       │   ├── MainActivity.kt              # 主界面
│   │       │   ├── TranslationAccessibilityService.kt  # 无障碍服务
│   │       │   └── OverlayService.kt            # 悬浮窗服务
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml        # 主界面布局
│   │       │   ├── xml/
│   │       │   │   └── accessibility_service_config.xml  # 无障碍服务配置
│   │       │   └── values/
│   │       │       └── strings.xml              # 字符串资源
│   │       ├── assets/
│   │       │   └── dict.json                    # 翻译词典
│   │       └── AndroidManifest.xml              # 应用清单
│   └── build.gradle                              # 应用配置
├── build.gradle                                  # 项目配置
├── settings.gradle                               # 项目设置
└── .github/workflows/build.yml                  # GitHub Actions工作流
```

## 如何构建APK

### 方法1：使用Android Studio（推荐）

1. 下载并安装 [Android Studio](https://developer.android.com/studio)
2. 打开Android Studio，选择 "Open an existing project"
3. 导航到项目目录并打开
4. 等待Gradle同步完成（第一次可能需要几分钟）
5. 点击 `Build` -> `Build Bundle(s) / APK(s)` -> `Build APK(s)`
6. APK文件会生成在 `app/build/outputs/apk/debug/` 目录

### 方法2：使用GitHub Actions自动构建

1. 将项目上传到GitHub仓库
2. 启用GitHub Actions
3. 每次push代码都会自动构建APK
4. 构建好的APK会作为artifact上传

### 方法3：使用命令行（需要安装Java和Gradle）

```bash
# 进入项目目录
cd TranslationOverlay

# 构建Debug APK
./gradlew assembleDebug

# 构建Release APK
./gradlew assembleRelease
```

## 使用方法

1. 安装构建好的APK到Android设备
2. 打开应用
3. 点击"开启无障碍权限"，在系统设置中找到并开启"悬浮翻译服务"
4. 点击"开启悬浮窗权限"，在系统设置中授予权限
5. 点击"启动翻译服务"
6. 打开谷歌商店或其他英文应用
7. 界面上的英文会自动被翻译成中文

## 自定义翻译词典

编辑 `app/src/main/assets/dict.json` 文件，添加或修改翻译词条：

```json
{
  "英文词条": "中文翻译",
  "另一个英文词条": "另一个中文翻译"
}
```

## 技术说明

- **无障碍服务**：用于读取屏幕上的文本内容和位置信息
- **悬浮窗服务**：用于在原文本位置显示翻译结果
- **本地词典**：使用JSON格式存储翻译词条，支持离线使用

## 注意事项

- 需要Android 5.0（API 21）或更高版本
- 首次使用需要开启无障碍权限和悬浮窗权限
- 建议仅在需要时启用翻译服务，以节省电池电量
- 某些应用可能有特殊的界面防护，可能无法翻译

## 许可证

MIT License
