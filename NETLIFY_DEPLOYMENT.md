# Netlify Deployment Configuration

## Environment Variables Required

### Android SDK Configuration
```
ANDROID_HOME=/opt/android/sdk
JAVA_HOME=/opt/java
```

### Optional: Firebase (for push notifications)
```
GOOGLE_SERVICES_JSON=your_google_services.json_content
```

### Optional: Zerodha API
```
ZERODHA_API_KEY=your_api_key
ZERODHA_API_SECRET=your_api_secret
```

## Netlify Dashboard Configuration

### Step 1: Connect Repository
1. Go to https://app.netlify.com
2. Click "Add new site" → "Import an existing project"
3. Select GitHub → "primeinvestorpro-ship-it/Hedge_NiftyOptions"

### Step 2: Configure Build Settings

**Base directory:** `app` (or leave empty for root)

**Build command:**
```bash
./gradlew assembleRelease
```

**Publish directory:**
```
app/build/outputs/apk/release/
```

**Functions directory:** (leave empty)

### Step 3: Add Environment Variables

In Netlify Dashboard → Site settings → Environment variables:

```bash
# Android SDK
ANDROID_HOME=/opt/android/sdk
JAVA_HOME=/opt/java

# Build optimization
GRADLE_OPTS=-Xmx2048m
```

### Step 4: Deploy

Click "Deploy site"

## Alternative: netlify.toml Configuration

Create `netlify.toml` in root directory:

```toml
[build]
  command = "./gradlew assembleRelease"
  publish = "app/build/outputs/apk/release/"
  base = "/"

[build.environment]
  ANDROID_HOME = "/opt/android"
  JAVA_HOME = "/opt/java"
  GRADLE_OPTS = "-Xmx2048m"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

## Troubleshooting

### Common Issues

1. **Build fails with SDK not found**
   - Ensure ANDROID_HOME is set correctly
   - Netlify build image includes Android SDK by default

2. **Gradle version mismatch**
   - Use Gradle 8.2 as specified in gradle-wrapper.properties

3. **Out of memory**
   - Increase GRADLE_OPTS: `-Xmx4096m`

### Check Build Status

Netlify will show build logs in real-time. Check for:
- Gradle download progress
- Dependency resolution
- Compilation errors
- APK generation

## After Deployment

1. Download APK from Netlify deploy logs
2. Enable "Install from unknown sources" on Android device
3. Transfer and install APK

## CI/CD Setup (Optional)

Enable automatic deployments:
1. Netlify Dashboard → Deploy settings
2. Enable "Build hooks"
3. Configure GitHub to trigger builds on push

## Security Notes

- Never commit API keys to repository
- Use Netlify environment variables for secrets
- Firebase config can be added later when ready
