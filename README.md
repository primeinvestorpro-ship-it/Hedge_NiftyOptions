# Hedge Nifty Options - Android App

An advanced Android application for intraday ATM options trading on NIFTY 50, featuring a premium **Make in India** theme, real-time data tracking, and strategy calculator.

## Features

- **Make in India Theme**: Premium UI with Saffron, White, and Green aesthetics
- **Live NSE Data**: Real-time NIFTY spot price, options chain, and market data
- **Strategy Calculator**: ATM options buying strategy with edge scoring and probability analysis
- **Simulated Trade Execution**: Execute trades in a simulated environment for safe practice
- **Push Notifications**: Instant alerts for trade signals
- **Trade History**: Track all executed trades with P/L indicators
- **Risk Management**: Built-in stop loss and profit target management

## Technical Stack

- **Frontend**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Repository pattern
- **Networking**: Retrofit for API calls
- **Dependency Injection**: Koin
- **Notifications**: Firebase Cloud Messaging
- **Data Models**: Clean separation of business and UI models

## Project Structure

```
Hedge_NiftyOptions/
├── app/
│   ├── src/main/
│   │   ├── java/com/hedgenifty/
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/               # Data models and repository
│   │   │   │   ├── OptionsRepository.kt
│   │   │   │   ├── ApiService.kt
│   │   │   │   └── DataModels.kt
│   │   │   ├── network/            # API services
│   │   │   │   └── NetworkManager.kt
│   │   │   ├── ui/
│   │   │   │   ├── screens/        # Compose screens
│   │   │   │   │   ├── MainScreen.kt
│   │   │   │   │   ├── LiveDataScreen.kt
│   │   │   │   │   ├── StrategyCalculatorScreen.kt
│   │   │   │   │   ├── TradeExecutionScreen.kt
│   │   │   │   │   └── TradeHistoryScreen.kt
│   │   │   │   └── theme/          # UI theme (India Palette)
│   │   │   ├── utils/              # Helper classes
│   │   │   │   ├── TradeViewModel.kt
│   │   │   │   └── TradeManager.kt
│   │   │   └── models/            # Shared models
│   │   │       └── TradeModels.kt
│   │   └── res/                   # Resources
│   │       └── drawable/          # Vector assets
│   │       └── values/            # String resources
│   └── build.gradle
├── build.gradle                    # Root build file
├── settings.gradle
└── README.md
```

## Setup Instructions

### 1. Prerequisites

- Android Studio Flamingo or later
- JDK 17 or later
- Android SDK 34

### 2. Clone and Build

```bash
git clone https://github.com/primeinvestorpro-ship-it/Hedge_NiftyOptions.git
cd Hedge_NiftyOptions
./gradlew assembleDebug
```

## Trading Strategy

### ATM Option Buying Strategy

- **Entry**: ATM Call, ATM Put, or ATM Long Straddle
- **Time Window**: Intraday (09:30-15:00 IST)
- **Edge Scoring**: 5-filter validation system (IV Velocity, Straddle Math, OI Micro, Gamma, Microstructure)

## License

This project is proprietary software. All rights reserved.

---

**Note**: This software is for educational purposes. Trading financial instruments involves significant risk.
