# 飲料糖分追蹤 App（DrinkSugar）

記錄每天喝的手搖／飲料，自動計算糖分與熱量，追蹤今日剩餘額度與歷史趨勢，並用規則式通知提醒——iOS App，本機資料、無伺服器。

這是一個「個人健康 App」三大支柱（**健身／飲食／情緒**）的第一步：先把**飲食支柱的「飲料糖分」**做完上架驗證，再逐步擴張。

## 現況

- ✅ **設計文件（spec）完成**：`docs/superpowers/specs/2026-07-09-飲料糖分追蹤-design.md`
- ✅ **實作計畫（16 個 TDD 任務）完成**：`docs/superpowers/plans/2026-07-09-飲料糖分追蹤-v1.md`
- ⏳ **待實作**：需 macOS ＋ Xcode 環境到位後開工（iOS 無法在 Windows 上編譯／測試）。

## 技術棧（V1）

- **平台**：iOS 16+（SwiftUI）
- **開發工具**：Xcode（macOS 專用）
- **資料**：本機 SQLite（GRDB.swift），無伺服器
- **統計圖**：Swift Charts
- **通知**：UserNotifications（規則式，非 AI）
- **變現**：AdMob（Google Mobile Ads SDK）
- **相依管理**：Swift Package Manager

## V1 範圍

記一杯（店家／品項／糖度／容量／加料，或手動輸入）→ 自動算糖分與熱量 → 今日剩餘額度環形進度 → 歷史／統計 → 每日糖分目標設定 → 規則式提醒與建議 → AdMob。

> 數值為**參考估算**，非醫療建議。

## 路線圖

- **V1**：飲料糖分追蹤 ＋ 規則式提醒／建議
- **V2**：食物／三餐糖分與營養、AI 個人化建議、無廣告＋雲端同步訂閱
- **V3**：血糖記錄（透過 Apple HealthKit 讀取 ＋ 手動輸入）
- **並行**：健身、情緒支柱掛入同一個 App 外殼

## 開始實作

在 macOS 上開啟本 repo，依照 `docs/superpowers/plans/` 的實作計畫逐一任務執行（每個任務都是 TDD：先寫測試 → 跑紅 → 實作 → 跑綠 → commit）。
