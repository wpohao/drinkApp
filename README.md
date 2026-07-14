# 飲料糖分追蹤 App（DrinkSugar）

記錄每天喝的手搖／飲料，自動計算糖分與熱量，追蹤今日剩餘額度與歷史趨勢，並用規則式通知提醒——**Android App**，本機資料、無伺服器。定位為「**控糖版手搖日記**」：保留手搖日記類 App 的討喜體驗（日曆／星級／圓餅圖／歷史快選），疊在我們獨有的「算糖＋每日控糖額度」核心上。

這是一個「個人健康 App」三大支柱（**健身／飲食／情緒**）的第一步：先把**飲食支柱的「飲料糖分」**做完上架驗證，再逐步擴張。

## 現況

- ✅ **設計文件（spec）完成**：`docs/superpowers/specs/2026-07-09-飲料糖分追蹤-design.md`（2026-07-14 改為 Android 版）
- ✅ **實作計畫（TDD 任務）完成**：`docs/superpowers/plans/2026-07-09-飲料糖分追蹤-v1.md`
- ⏳ **待實作**：用 Android Studio 開工即可（**Windows 可全程開發／build／測試／上架，不需 Mac**）。

## 技術棧（V1）

- **平台**：Android 8.0+（minSdk 26）
- **語言／UI**：Kotlin ＋ Jetpack Compose
- **開發工具**：Android Studio（Windows／macOS／Linux 皆可）
- **資料**：本機 SQLite（Room），無伺服器
- **統計圖**：Compose Canvas 自繪（趨勢折線／圓餅圖／日曆，避免第三方圖表庫版本風險）
- **通知**：NotificationManager ＋ WorkManager（規則式，非 AI）
- **變現**：AdMob（Google Mobile Ads Android SDK）
- **相依管理**：Gradle（Kotlin DSL ＋ Version Catalog）
- **測試**：JUnit ＋ Turbine（Flow 測試）＋ Room in-memory

## V1 範圍

記一杯（店家／品項／糖度／容量／加料／冰塊／星級，或手動輸入）→ 自動算糖分與熱量 → 今日剩餘額度環形進度 → 歷史快選 → 日曆視圖（控糖顏色）／圓餅圖／趨勢統計 → 月度戰績分享圖 → 每日糖分目標設定 → 規則式提醒與建議 → AdMob。

> 數值為**參考估算**，非醫療建議。

## 路線圖

- **V1**：Android 控糖手搖日記 ＋ 規則式提醒／建議
- **V2**：食物／三餐糖分與營養、AI 個人化建議、無廣告＋雲端同步訂閱（Google Play Billing）
- **V3**：血糖記錄（透過 Android Health Connect 讀取 ＋ 手動輸入）
- **iOS 移植**：Android 驗證成功後用 SwiftUI 重寫 UI 移植（Core 邏輯已分層隔離）
- **並行**：健身、情緒支柱掛入同一個 App 外殼

## 開始實作

用 Android Studio 開啟本 repo，依照 `docs/superpowers/plans/` 的實作計畫逐一任務執行（每個任務都是 TDD：先寫測試 → 跑紅 → 實作 → 跑綠 → commit）。
