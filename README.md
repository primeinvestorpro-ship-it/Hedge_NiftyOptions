# Hedge Nifty Options - Android App

An Android application for intraday ATM options trading on NIFTY 50 with live data tracking, strategy calculator, and Zerodha Kite API integration.

## Features

- **Live NSE Data**: Real-time NIFTY spot price, options chain, and market data
- **Strategy Calculator**: ATM options buying strategy with edge scoring and probability analysis
- **Trade Execution**: Place bracket orders via Zerodha Kite API
- **Push Notifications**: Instant alerts for trade signals
- **Trade History**: Track all executed trades with P/L
- **Risk Management**: Built-in stop loss and profit target management

## Technical Stack

- **Frontend**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Repository pattern
- **Networking**: Retrofit for API calls
- **Database**: SharedPreferences for local storage
- **Notifications**: Firebase Cloud Messaging
- **API Integration**: Zerodha Kite for order execution

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
│   │   │   │   └── theme/          # UI theme
│   │   │   ├── utils/              # Helper classes
│   │   │   │   ├── TradeViewModel.kt
│   │   │   │   ├── TradeManager.kt
│   │   │   │   └── ZerodhaManager.kt
│   │   │   └── models/            # Data models
│   │   │       └── TradeModels.kt
│   │   └── res/                   # Resources
│   │       └── values/
│   │           └── strings.xml
│   └── build.gradle
├── build.gradle                    # Root build file
├── settings.gradle
├── gradle.properties
├── local.properties
├── netlify.toml                   # Netlify configuration
└── README.md
```

## Setup Instructions

### 1. Prerequisites

- Android Studio Arctic Fox or later
- JDK 11 or later
- Gradle 8.2 or later
- Android SDK 34 (API level 34)
- Git

### 2. Clone the Repository

```bash
git clone https://github.com/primeinvestorpro-ship-it/Hedge_NiftyOptions.git
cd Hedge_NiftyOptions
```

### 3. Configure Android SDK

Edit `local.properties` and set your Android SDK path:

```properties
sdk.dir=/path/to/your/android-sdk
```

### 4. Configure Firebase

1. Create a Firebase project at https://console.firebase.google.com
2. Add an Android app to your Firebase project
3. Download `google-services.json` and place it in `app/`
4. Enable Firebase Cloud Messaging in Firebase Console

### 5. Configure Zerodha Kite API

1. Register at https://developers.kite.trade
2. Create a new app and get API key and secret
3. Add credentials to `app/src/main/java/com/hedgenifty/utils/ZerodhaManager.kt`

### 6. Build the Project

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

### 7. Run Tests

```bash
# Unit tests
./gradlew test

# Lint checks
./gradlew lint
```

## Configuration Requirements

### Firebase Configuration

Required files:
- `app/google-services.json` - Firebase project configuration

Enable in Firebase Console:
- Firebase Cloud Messaging
- Firebase Analytics

### Zerodha Kite API

Required credentials:
- API Key
- API Secret
- User session tokens

Permissions needed:
- Order placement
- Position tracking
- Margin access

### Netlify Deployment

Build command:
```bash
./gradlew assembleRelease
```

Publish directory:
```
app/build/outputs/apk/release/
```

## Trading Strategy Implementation

### ATM Option Buying Strategy

- **Entry**: ATM Call, ATM Put, or ATM Long Straddle
- **Time Window**: Intraday only (09:30-15:00 IST)
- **Expiry**: Nearest weekly expiry
- **Risk Profile**: High-risk, option buyer

### Edge Scoring System (0-100)

1. **IV Velocity Filter**: IV acceleration > theta decay
2. **Straddle Math Filter**: Expected move ≥ 0.9 × Straddle price
3. **OI Micro Filter**: OI change rate > threshold
4. **Gamma Amplification Filter**: ATM gamma > 5-day median
5. **Microstructure Filter**: Tightened bid-ask + volume spike

**Edge requirement**: ≥4 of 5 filters must pass

### Risk Management

- Position sizing: % of risk capital
- Stop loss: Hard stop at defined percentage
- Profit target: Defined based on strategy
- Theta burn: Monitored per 15 minutes
- IV crush risk: Calculated probability

## API Endpoints

### NSE Data

- Options Chain Data
- Live Spot Price
- IV and Greeks
- OI and Volume

### Zerodha Kite

- Place Order (Bracket Order)
- Order History
- Positions
- Margins

## Push Notification Setup

### Notification Channels

Create notification channels for:
- Trade signals
- Price alerts
- System notifications

### FCM Integration

1. Add Firebase to project
2. Implement FCM service
3. Handle notification clicks
4. Send trade signals

## Security Considerations

- **API Keys**: Store in secure configuration
- **User Authentication**: Use Zerodha session tokens
- **Data Privacy**: Follow data protection guidelines
- **Network Security**: Use HTTPS for all API calls
- **Code Obfuscation**: Enable ProGuard for release builds

## Development Workflow

### Adding New Features

1. Create data models in `models/`
2. Add API endpoints in `network/`
3. Create UI screens in `ui/screens/`
4. Add business logic in `utils/`
5. Update navigation in `MainActivity.kt`

### Testing

1. Write unit tests for data models
2. Create integration tests for API calls
3. Test UI with Compose preview
4. Perform E2E testing on real device

## Deployment

### Local Testing

```bash
# Install dependencies
./gradlew dependencies

# Debug build
./gradlew assembleDebug

# Run on emulator/device
./gradlew installDebug
```

### Netlify Deployment

1. Push code to GitHub
2. Connect Netlify to GitHub repository
3. Configure build settings
4. Set environment variables
5. Deploy

### APK Distribution

- Debug APK: `app/build/outputs/apk/debug/`
- Release APK: `app/build/outputs/apk/release/`

## Troubleshooting

### Common Issues

1. **Build failures**
   - Check Android SDK path
   - Verify Gradle version
   - Check dependency versions

2. **API errors**
   - Verify API credentials
   - Check network connectivity
   - Review API rate limits

3. **UI issues**
   - Check Compose compatibility
   - Verify theme configuration
   - Test on different screen sizes

### Debug Commands

```bash
# Clean build
./gradlew clean

# Rebuild
./gradlew assembleDebug

# Run with debug output
./gradlew assembleDebug --info
```

## License

This project is proprietary software. All rights reserved.

## Support

For support, please open an issue on GitHub or contact the development team.

---

**Note**: This software involves financial trading and should be used with caution. Always understand the risks before trading options.