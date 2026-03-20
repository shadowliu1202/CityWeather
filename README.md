# 🌤️ Weather App

一款以 Jetpack Compose 打造的 Android 天氣應用程式，採用多模組架構，串接 OpenWeatherMap 即時 API 顯示當前城市天氣與一週預報。

---

## 🤖 AI 協作流程

本專案全程結合多款 AI 工具分工完成，最後透過Claude做總結，以下說明各工具負責的範圍：

| AI 工具           | 負責範圍                                                                                                              |
|-----------------|-------------------------------------------------------------------------------------------------------------------|
| **Stitch**      | 產生參考 UX/UI 設計稿與視覺草稿，定義深海軍藍主題、卡片排版、底部導覽列等畫面風格                                                                      |
| **Claude Code** | 根據 Stitch 草稿產生多模組專案基本框架（模組結構、Hilt DI、Navigation、UiState、ViewModel），並協助研究 OpenWeatherMap API 端點用法、整合 Ktor 網路層及 DTO |
| **Gemini AI**   | 協助除錯 Gradle 設定（KSP 版本衝突、AGP 9.x sourceSets 問題）、產生 Compose `@Preview`，以及查詢相關 Android API 文件（`java.time`）           |

---

## 📱 畫面功能

| 畫面               | 說明                                                  |
|------------------|-----------------------------------------------------|
| **WeatherRoute** | 主畫面：城市名稱、當前溫度、天氣狀況、體感溫度 / 濕度 / 風速、逐小時預報（橫向捲動）、7 天預報 |
| **CityRoute**    | 城市搜尋畫面：輸入關鍵字即時篩選城市清單，選取後跳轉至對應城市天氣                   |

---

## 🏗️ 模組架構

```
:app
 ├── :feature:weather       ← 天氣主畫面
 │    ├── :core:model
 │    └── :core:ui
 └── :feature:city          ← 城市搜尋畫面
      ├── :core:model
      └── :core:ui
```

### 模組職責

| 模組                 | 職責                                                                          |
|--------------------|-----------------------------------------------------------------------------|
| `:app`             | `WeatherApplication`（Hilt 入口）、`MainActivity`、`WeatherNavGraph`              |
| `:feature:weather` | 天氣顯示功能：ViewModel、UiState、Compose UI、Ktor 網路層、Hilt DI 模組                     |
| `:feature:city`    | 城市搜尋功能：ViewModel、UiState、Compose UI、Fake 資料、Hilt DI 模組                      |
| `:core:model`      | 共用資料模型：`City`、`Weather`、`HourlyForecast`、`DailyForecast`、`WeatherCondition` |
| `:core:ui`         | 共用主題：`WeatherTheme`、`Color`、`Typography`                                    |

---

## 📂 feature:weather 內部結構

```
feature/weather/
└── src/main/java/com/weather/feature/weather/
    ├── di/
    │   └── DataModule.kt              ← Hilt @Provides：HttpClient、Service、Repository
    ├── domain/
    │   └── WeatherRepository.kt       ← 介面：suspend getWeather(city): Weather
    ├── infra/
    │   ├── adapter/
    │   │   ├── Mapper.kt              ← DTO → Domain Model 轉換邏輯
    │   │   └── OpenWeatherMapWeatherRepository.kt  ← Repository 實作
    │   └── remote/
    │       ├── OpenWeatherMapService.kt   ← Ktor HttpClient（2 個端點）
    │       └── dto/
    │           ├── CurrentWeatherDto.kt   ← @Serializable：當前天氣 JSON 結構
    │           └── ForecastDto.kt         ← @Serializable：5 天預報 JSON 結構
    └── presentation/
        ├── WeatherRoute.kt            ← 主畫面 Composable
        ├── WeatherViewModel.kt        ← @HiltViewModel，loadWeather(city)
        ├── WeatherUiState.kt          ← Loading | Success | Error
        ├── WeatherIconUtil.kt         ← WeatherCondition → Material Icon + 顏色
        ├── today/
        │   ├── HourlySection.kt       ← 「Today」區塊標題列
        │   ├── HourlyForecastRow.kt   ← LazyRow 逐小時卡片
        │   └── WeatherStatCard.kt     ← 體感溫度 / 濕度 / 風速三卡片
        └── weekly/
            ├── WeeklySection.kt       ← 「7-Day Forecast」區塊標題列
            └── WeeklyForecastSection.kt ← LazyRow 每日預報卡片
```

---

## 🌐 OpenWeatherMap API

> API 文件：https://openweathermap.org/api

### 使用端點

| 端點                                                              | 用途                                      |
|-----------------------------------------------------------------|-----------------------------------------|
| `GET /data/2.5/weather?q={城市}&appid={key}&units=metric`         | 取得當前天氣（溫度、濕度、風速、天氣代碼）                   |
| `GET /data/2.5/forecast?q={城市}&appid={key}&units=metric&cnt=40` | 取得 5 天 / 每 3 小時預報（最多 40 筆），用於逐小時與 7 日摘要 |

### 天氣代碼對應

| OpenWeatherMap ID 範圍 | 對應 `WeatherCondition` |
|----------------------|-----------------------|
| 200–299              | `STORMY`（雷陣雨）         |
| 300–399 / 500–599    | `RAINY`（毛毛雨 / 雨）      |
| 600–699              | `SNOWY`（雪）            |
| 700–799              | `FOGGY`（霧 / 霾）        |
| 800                  | `SUNNY`（晴天）           |
| 801–802              | `PARTLY_CLOUDY`（少雲）   |
| 803–804              | `CLOUDY`（多雲）          |

### 資料換算

| 原始值         | 換算                                     |
|-------------|----------------------------------------|
| 風速（m/s）     | × 3.6 → km/h                           |
| Unix 時間戳    | `java.time.Instant` → "Monday, 12 Oct" |
| `dt_txt` 字串 | substring → "14:00" 逐小時 / 星期幾          |

---

## 🔧 技術棧

| 類別     | 工具 / 版本                                   |
|--------|-------------------------------------------|
| 語言     | Kotlin 2.3.20                             |
| UI     | Jetpack Compose（BOM 2026.03.00）、Material3 |
| DI     | Hilt 2.59.2 + KSP 2.3.6                   |
| 網路     | Ktor 3.4.1（OkHttp 引擎）                     |
| 序列化    | kotlinx.serialization 1.10.0              |
| 導覽     | Navigation Compose 2.9.7                  |
| 架構     | AGP 9.0.1、Gradle 9.1.0                    |
| 最低 SDK | API 26（Android 8.0）                       |

---

## 🎨 設計主題

| 色彩 Token | Hex             |
|----------|-----------------|
| 背景       | `#0D1B2E`（深海軍藍） |
| 卡片背景     | `#1A2E45`       |
| 選取卡片     | `#1E3A5F`       |
| 強調藍      | `#4A90D9`       |
| 太陽黃      | `#FFC107`       |
| 雨水藍      | `#4FC3F7`       |

---

## ⚙️ 開始使用

### 1. 取得 API 金鑰

前往 [openweathermap.org](https://openweathermap.org/api) 免費註冊並取得 API Key。

### 2. 設定金鑰

在專案根目錄的 `local.properties` 加入：

```properties
OPENWEATHER_API_KEY=your_api_key_here
```

### 3. 開啟專案

用 Android Studio 開啟 `Weather/` 資料夾，Gradle Sync 後即可執行。

---

## 🏛️ 架構總結

本專案採用 **Feature-First 多模組架構**，結合 **Clean Architecture 分層**：

```
Compose UI  ──→  ViewModel (Hilt)  ──→  Repository (interface)
                                              │
                              ┌───────────────┘
                              ↓
                    OpenWeatherMapWeatherRepository
                              │
                    ┌─────────┴──────────┐
                    ↓                    ↓
            OpenWeatherMapService     Mapper.kt
            (Ktor HttpClient)    (DTO → Domain)
                    │
                    ↓
            OpenWeatherMap REST API
```

- **Presentation 層**：Compose + ViewModel + UiState（單向資料流）
- **Domain 層**：Repository 介面（不依賴任何框架）
- **Infrastructure 層**：Ktor 網路呼叫、DTO、Mapper（可隨時抽換實作）
- **DI**：Hilt 統一管理所有依賴，`@Singleton` 確保 HttpClient 唯一實例
