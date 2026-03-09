# 📱 ITGAdsSDK — AdMob Next Gen Library

> **Thư viện quản lý quảng cáo AdMob toàn diện cho Android**, hỗ trợ Banner, Native, Interstitial, Rewarded, Open App Ads với cấu hình linh hoạt qua JSON & Firebase Remote Config.

---

## 📋 Mục lục

- [Tổng quan](#-tổng-quan)
- [Yêu cầu hệ thống](#-yêu-cầu-hệ-thống)
- [Cài đặt](#-cài-đặt)
- [Cấu hình](#-cấu-hình)
  - [1. ad_config.json](#1-ad_configjson)
  - [2. Khởi tạo SDK](#2-khởi-tạo-sdk)
- [Sử dụng](#-sử-dụng)
  - [Banner Ads](#banner-ads)
  - [Interstitial Ads](#interstitial-ads)
  - [Interstitial Splash Ads](#interstitial-splash-ads)
  - [Native Ads](#native-ads)
  - [Rewarded Ads](#rewarded-ads)
  - [Rewarded Interstitial Ads](#rewarded-interstitial-ads)
  - [Open App Ads (Resume)](#open-app-ads-resume)
  - [Open App Ads (Splash)](#open-app-ads-splash)
- [CMP (Consent Management)](#-cmp-consent-management)
- [Cấu hình nâng cao](#-cấu-hình-nâng-cao)
- [API Reference](#-api-reference)
- [Kiến trúc thư viện](#-kiến-trúc-thư-viện)
- [FAQ & Troubleshooting](#-faq--troubleshooting)

---

## 🌟 Tổng quan

**ITGAdsSDK** cung cấp một lớp abstraction đầy đủ cho Google AdMob, giúp bạn:

- ✅ **Quản lý tất cả loại quảng cáo** (Banner, Native, Interstitial, Rewarded, Open App)
- ✅ **Cấu hình placement** qua file JSON hoặc Firebase Remote Config
- ✅ **Tự động xử lý CMP** (Consent Management Platform) theo quy định GDPR
- ✅ **Tích hợp MMP** (Adjust, Facebook Analytics, Firebase Analytics)
- ✅ **Hỗ trợ caching** — tải quảng cáo trước và hiển thị khi cần
- ✅ **Resume Ads** — Tự động hiển thị quảng cáo khi người dùng quay lại ứng dụng
- ✅ **Thread-safe** — tự động dispatch callback về Main Thread

---

## 📦 Yêu cầu hệ thống

| Yêu cầu | Phiên bản |
|---------|-----------|
| Android `minSdk` | **24** (Android 7.0+) |
| Android `compileSdk` | **36** |
| Kotlin | 1.9+ |
| Java | 11 |

### Dependencies chính:

- Google AdMob SDK (`ads-mobile-sdk`)
- Firebase Remote Config
- Firebase Analytics
- Adjust SDK
- Facebook SDK
- Moshi (JSON parsing)
- Timber (Logging)
- Shimmer (Loading effects)
- Lottie (Animations)

---

## 🔧 Cài đặt

### Bước 1: Thêm module `mylibrary` vào dự án

Trong `settings.gradle`:

```groovy
include ':mylibrary'
```

### Bước 2: Thêm dependency vào `app/build.gradle`:

```groovy
dependencies {
    implementation project(':mylibrary')
}
```

### Bước 3: Cấu hình `AndroidManifest.xml`

Đảm bảo bạn đã khai báo AdMob App ID:

```xml
<manifest>
    <application>
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX" />
    </application>
</manifest>
```

---

## ⚙️ Cấu hình

### 1. `ad_config.json`

Tạo file `app/src/main/assets/ad_config.json` để khai báo các placement quảng cáo:

```json
{
  "global": {
    "enable": true,
    "cmp_auto": true,
    "min_interval": 30000
  },
  "placements": [
    {
      "placement": "BANNER",
      "adsType": "banner",
      "ids": ["ca-app-pub-3940256099942544/6300978111"],
      "enable": true,
      "minInterval": 30000
    },
    {
      "placement": "BANNER_COLLAPSIBLE",
      "adsType": "banner",
      "ids": ["ca-app-pub-3940256099942544/9214589741"],
      "enable": true,
      "minInterval": 30000
    },
    {
      "placement": "NATIVE",
      "adsType": "native",
      "ids": ["ca-app-pub-3940256099942544/2247696110"],
      "enable": true,
      "minInterval": 30000
    },
    {
      "placement": "INTERSTITIAL_SPLASH",
      "adsType": "interstitial",
      "ids": ["ca-app-pub-3940256099942544/1033173712"],
      "enable": true,
      "minInterval": 30000
    },
    {
      "placement": "INTERSTITIAL_ALL",
      "adsType": "interstitial",
      "ids": ["ca-app-pub-3940256099942544/1033173712"],
      "enable": true,
      "minInterval": 30000
    },
    {
      "placement": "REWARDED",
      "adsType": "reward",
      "ids": ["ca-app-pub-3940256099942544/5224354917"],
      "enable": true,
      "minInterval": 30000
    },
    {
      "placement": "OPEN_RESUME",
      "adsType": "open_app",
      "ids": ["ca-app-pub-3940256099942544/9257395921"],
      "enable": true,
      "minInterval": 0
    }
  ]
}
```

#### Giải thích cấu trúc:

| Field | Mô tả |
|-------|--------|
| `global.enable` | Bật/tắt toàn bộ quảng cáo (`true`/`false`) |
| `global.cmp_auto` | Tự động hiển thị CMP consent form |
| `global.min_interval` | Khoảng cách tối thiểu giữa 2 lần hiển thị quảng cáo (ms) |
| `placement` | Tên định danh duy nhất cho mỗi vị trí quảng cáo |
| `adsType` | Loại quảng cáo: `banner`, `native`, `interstitial`, `reward`, `reward_inter`, `open_app` |
| `ids` | Danh sách Ad Unit ID (hỗ trợ waterfall nhiều ID) |
| `enable` | Bật/tắt riêng lẻ từng placement |
| `minInterval` | Khoảng cách tối thiểu cho placement này (ms) |

> 💡 **Tip:** Bạn có thể override config từ **Firebase Remote Config** với cùng JSON key. Config Remote sẽ ưu tiên hơn file local.

---

### 2. Khởi tạo SDK

Khởi tạo trong class `Application`:

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Cấu hình SDK
        val config = ITGAdsSDKConfig(
            appId = "ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX", // AdMob App ID
            debug = BuildConfig.DEBUG,
            testDevices = listOf("YOUR_TEST_DEVICE_ID"),
            minInterval = 30L,
            adjustToken = "YOUR_ADJUST_TOKEN",
            adjustTokenTiktok = "YOUR_ADJUST_TIKTOK_TOKEN",
            facebookId = "YOUR_FACEBOOK_APP_ID",
            facebookClientToken = "YOUR_FACEBOOK_CLIENT_TOKEN"
        )

        // Khởi tạo
        ITGAdsSDK.initialize(this, config)

        // Đăng ký Open Resume Ads sau khi AdMob sẵn sàng
        ITGAdsSDK.onAdMobReady {
            ITGAdsOpenResume.registerAndLoad("OPEN_RESUME", null)
        }
    }
}
```

#### `ITGAdsSDKConfig` — Tham số cấu hình:

| Tham số | Kiểu | Mặc định | Mô tả |
|---------|------|----------|--------|
| `appId` | `String` | Test ID | AdMob Application ID |
| `debug` | `Boolean` | `false` | Chế độ debug |
| `testDevices` | `List<String>` | `emptyList()` | Danh sách test device ID |
| `minInterval` | `Long` | `30L` | Khoảng cách tối thiểu (giây) |
| `adjustToken` | `String` | `""` | Token Adjust |
| `adjustTokenTiktok` | `String` | `""` | Token Adjust cho TikTok |
| `facebookId` | `String` | `""` | Facebook App ID |
| `facebookClientToken` | `String` | `""` | Facebook Client Token |

---

## 🚀 Sử dụng

### Banner Ads

**ITGAdsBanner** hỗ trợ nhiều kiểu banner khác nhau:

#### Banner Inline Adaptive

```kotlin
ITGAdsBanner.showInlineAdaptive(
    placement = "BANNER",
    viewGroup = findViewById(R.id.bannerContainer),
    forceRefresh = false,  // true = bỏ cache, tải lại
    center = true,         // Căn giữa trong container
    callback = object : ITGBannerCallback() {
        override fun onLoaded(adView: AdView, placement: String) {
            Log.d("Ads", "Banner loaded: $placement")
        }

        override fun onFailed(placement: String, error: ITGBannerError) {
            Log.e("Ads", "Banner failed: ${error.message}")
        }

        override fun onImpression(placement: String) {
            Log.d("Ads", "Banner impression: $placement")
        }

        override fun onClicked(placement: String) {
            Log.d("Ads", "Banner clicked: $placement")
        }

        override fun onPaid(placement: String, revenue: AdRevenue) {
            Log.d("Ads", "Banner revenue: ${revenue.value} ${revenue.currencyCode}")
        }
    }
)
```

#### Banner Anchored Adaptive

```kotlin
ITGAdsBanner.showAnchoredAdaptive(
    placement = "BANNER",
    viewGroup = findViewById(R.id.bannerContainer),
    position = ITGAnchorPosition.BOTTOM, // BOTTOM hoặc TOP
    forceRefresh = false,
    center = true,
    callback = object : ITGBannerCallback() {
        override fun onLoaded(adView: AdView, placement: String) {
            Log.d("Ads", "Anchored banner loaded")
        }

        override fun onFailed(placement: String, error: ITGBannerError) {
            Log.e("Ads", "Anchored banner failed: ${error.message}")
        }
    }
)
```

#### Banner Collapsible

```kotlin
ITGAdsBanner.showCollapsible(
    placement = "BANNER_COLLAPSIBLE",
    viewGroup = findViewById(R.id.bannerContainer),
    position = ITGCollapsiblePosition.BOTTOM, // TOP hoặc BOTTOM
    forceRefresh = false,
    center = true,
    callback = object : ITGBannerCallback() {
        override fun onLoaded(adView: AdView, placement: String) {
            Log.d("Ads", "Collapsible banner loaded")
        }

        override fun onFailed(placement: String, error: ITGBannerError) {
            Log.e("Ads", "Collapsible banner failed: ${error.message}")
        }

        override fun onCollapsed(placement: String, position: ITGCollapsiblePosition) {
            Log.d("Ads", "Banner collapsed at position: $position")
        }
    }
)
```

#### Banner Fixed Size

```kotlin
ITGAdsBanner.show(
    placement = "BANNER",
    viewGroup = findViewById(R.id.bannerContainer),
    adSize = AdSize.BANNER,  // BANNER, LARGE_BANNER, MEDIUM_RECTANGLE, ...
    forceRefresh = false,
    center = true,
    callback = object : ITGBannerCallback() {
        override fun onLoaded(adView: AdView, placement: String) {
            Log.d("Ads", "Fixed banner loaded")
        }

        override fun onFailed(placement: String, error: ITGBannerError) {
            Log.e("Ads", "Fixed banner failed: ${error.message}")
        }
    }
)
```

#### Preload Banner (tải trước, hiển thị sau)

```kotlin
ITGAdsBanner.preload(
    placement = "BANNER",
    forceRefresh = false,
    callback = object : ITGBannerCallback() {
        override fun onLoaded(adView: AdView, placement: String) {
            // Banner đã sẵn sàng trong cache
            // Gọi show() sau để hiển thị
        }

        override fun onFailed(placement: String, error: ITGBannerError) {
            Log.e("Ads", "Preload failed: ${error.message}")
        }
    }
)
```

#### Xóa cache Banner

```kotlin
// Xóa cache của một placement
ITGAdsBanner.clearCache("BANNER")

// Xóa toàn bộ cache banner
ITGAdsBanner.clearAllCache()
```

#### Layout XML cho Banner

```xml
<!-- Banner Container -->
<FrameLayout
    android:id="@+id/bannerContainer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentBottom="true">

    <!-- Shimmer loading placeholder (optional) -->
    <com.facebook.shimmer.ShimmerFrameLayout
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:background="@color/black" />
</FrameLayout>
```

---

### Interstitial Ads

#### Preload (tải trước)

```kotlin
ITGIAdsInter.preload(
    placement = "INTERSTITIAL_ALL",
    forceRefresh = false,
    callback = object : ITGInterCallback() {
        override fun onLoaded(placement: String) {
            Log.d("Ads", "Interstitial loaded: $placement")
        }

        override fun onFailed(placement: String, error: ITGInterError) {
            Log.e("Ads", "Interstitial failed: ${error.message}")
        }
    }
)
```

#### Show (hiển thị)

```kotlin
ITGIAdsInter.show(
    placement = "INTERSTITIAL_ALL",
    activity = this,
    forceShow = false,            // true = bỏ qua min interval
    loadIfNotAvailable = true,    // true = tự động tải nếu chưa có
    showLoading = true,           // true = hiển thị loading dialog
    customLoadingView = null,     // View custom cho loading
    callback = object : ITGInterCallback() {
        override fun onLoaded(placement: String) {
            Log.d("Ads", "Interstitial loaded")
        }

        override fun onFailed(placement: String, error: ITGInterError) {
            Log.e("Ads", "Show failed: ${error.message}")
        }

        override fun onDismissed(placement: String) {
            // Quảng cáo đã đóng — chuyển màn hình hoặc tiếp tục logic
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
        }

        override fun onImpression(placement: String) {
            Log.d("Ads", "Impression recorded")
        }

        override fun onClicked(placement: String) {
            Log.d("Ads", "Ad clicked")
        }

        override fun onPaid(placement: String, revenue: AdRevenue) {
            Log.d("Ads", "Revenue: ${revenue.value}")
        }

        override fun onAdLeftApplication(placement: String) {
            Log.d("Ads", "User left app")
        }
    }
)
```

#### Kiểm tra trạng thái

```kotlin
// Kiểm tra đã load chưa
val isLoaded = ITGIAdsInter.isLoaded("INTERSTITIAL_ALL")

// Kiểm tra đã sẵn sàng show chưa (đã load + đủ interval)
val isReady = ITGIAdsInter.isReady("INTERSTITIAL_ALL")

// Xóa cache
ITGIAdsInter.clearCache("INTERSTITIAL_ALL")
ITGIAdsInter.clearCache() // xóa tất cả
```

---

### Interstitial Splash Ads

Dùng cho **màn hình Splash** — tự động chờ quảng cáo tải xong rồi hiển thị, có timeout:

```kotlin
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ITGAdsSDK.onAdMobReady(timeoutMs = 7000) {
            ITGAdsInterSplash.show(
                placement = "INTERSTITIAL_SPLASH",
                activity = this,
                timeoutMs = 15_000L,       // Timeout chờ ad load (ms)
                showLoading = true,         // Hiển thị loading
                customLoadingView = null,   // Custom loading view
                showAdCallback = object : ITGInterCallback() {
                    override fun onDismissed(placement: String) {
                        // Quảng cáo đóng → chuyển sang MainActivity
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ITGAdsInterSplash.cancel() // Hủy timer nếu activity bị destroy
    }
}
```

> ⚠️ **Lưu ý:** `onDismissed` luôn được gọi — kể cả khi ad load thất bại hoặc timeout. Đặt logic chuyển màn hình trong đây.

---

### Native Ads

**ITGAdsNative** hỗ trợ nhiều kích thước layout có sẵn:

#### Show trực tiếp (load + show)

```kotlin
// Native Large
ITGAdsNative.showLarge(
    placement = "NATIVE",
    viewGroup = findViewById(R.id.nativeAdContainer),
    layoutRes = R.layout.layout_native_large,
    shimmerLayoutRes = R.layout.ads_shimmer_native_large,
    callback = object : ITGNativeCallback() {
        override fun onLoaded(nativeAdView: NativeAdView, nativeAd: NativeAd, placement: String) {
            Log.d("Ads", "Native ad loaded")
        }

        override fun onFailed(placement: String, error: ITGNativeError) {
            Log.e("Ads", "Native failed: ${error.message}")
        }

        override fun onImpression(placement: String) {
            Log.d("Ads", "Native impression")
        }

        override fun onClicked(placement: String) {
            Log.d("Ads", "Native clicked")
        }

        override fun onPaid(placement: String, revenue: AdRevenue) {
            Log.d("Ads", "Native revenue: ${revenue.value}")
        }
    }
)
```

#### Các loại Native Layout

```kotlin
// Small
ITGAdsNative.showSmall(
    placement = "NATIVE",
    viewGroup = findViewById(R.id.nativeContainer),
    layoutRes = R.layout.layout_native_small,
    shimmerLayoutRes = R.layout.ads_shimmer_native_small,
    callback = object : ITGNativeCallback() { /* ... */ }
)

// Medium
ITGAdsNative.showMedium(
    placement = "NATIVE",
    viewGroup = findViewById(R.id.nativeContainer),
    layoutRes = R.layout.layout_native_medium,
    shimmerLayoutRes = R.layout.ads_shimmer_native_medium,
    callback = object : ITGNativeCallback() { /* ... */ }
)

// Small as Banner
ITGAdsNative.showSmallAsBanner(
    placement = "NATIVE",
    viewGroup = findViewById(R.id.nativeContainer),
    layoutRes = R.layout.layout_native_small_as_banner,
    shimmerLayoutRes = R.layout.ads_shimmer_native_small_as_banner,
    callback = object : ITGNativeCallback() { /* ... */ }
)

// Fullscreen
ITGAdsNative.showFullscreen(
    placement = "NATIVE",
    viewGroup = findViewById(R.id.nativeContainer),
    layoutRes = R.layout.layout_native_large_full_screen,
    shimmerLayoutRes = R.layout.ads_shimmer_native_large_full_screen,
    callback = object : ITGNativeCallback() { /* ... */ }
)
```

#### Media Aspect Ratio (Custom tỷ lệ video/ảnh)

Tất cả `show*()` đều hỗ trợ tham số `mediaAspectRatio` để tùy chỉnh tỷ lệ khung hình cho media (video hoặc ảnh):

```kotlin
// Auto — Tự động detect ratio từ nội dung ad (mặc định, khuyến nghị)
ITGAdsNative.showLarge(
    placement = "NATIVE",
    viewGroup = nativeContainer,
    mediaAspectRatio = MediaAspectRatio.Auto,
    callback = object : ITGNativeCallback() { /* ... */ }
)

// Fixed 16:9 — Cố định tỷ lệ cho video
ITGAdsNative.showLarge(
    placement = "NATIVE",
    viewGroup = nativeContainer,
    mediaAspectRatio = MediaAspectRatio.RATIO_16_9,
    callback = object : ITGNativeCallback() { /* ... */ }
)

// Custom ratio — Tỷ lệ tùy ý (VD: 3:4 dọc)
ITGAdsNative.showLarge(
    placement = "NATIVE",
    viewGroup = nativeContainer,
    mediaAspectRatio = MediaAspectRatio.Fixed(3, 4),
    callback = object : ITGNativeCallback() { /* ... */ }
)

// FillParent — Lấp đầy container (behavior gốc, MATCH_PARENT x MATCH_PARENT)
ITGAdsNative.showLarge(
    placement = "NATIVE",
    viewGroup = nativeContainer,
    mediaAspectRatio = MediaAspectRatio.FillParent,
    callback = object : ITGNativeCallback() { /* ... */ }
)
```

**Các ratio có sẵn:**

| Constant | Tỷ lệ | Mô tả |
|----------|--------|--------|
| `MediaAspectRatio.Auto` | Tự động | Detect từ `mediaContent.aspectRatio`, fallback 16:9 |
| `MediaAspectRatio.RATIO_16_9` | 16:9 | Phổ biến nhất cho video |
| `MediaAspectRatio.RATIO_4_3` | 4:3 | Truyền thống |
| `MediaAspectRatio.RATIO_1_1` | 1:1 | Vuông |
| `MediaAspectRatio.RATIO_3_2` | 3:2 | Ảnh chụp |
| `MediaAspectRatio.RATIO_21_9` | 21:9 | Ultrawide |
| `MediaAspectRatio.Fixed(w, h)` | Custom | Bất kỳ tỷ lệ nào |
| `MediaAspectRatio.FillParent` | — | Lấp đầy parent (MATCH_PARENT) |

#### Kiểm tra loại media (Video hay Ảnh)

Trong callback `onLoaded`, bạn có thể kiểm tra nội dung ad là video hay ảnh:

```kotlin
ITGAdsNative.showLarge(
    placement = "NATIVE",
    viewGroup = nativeContainer,
    callback = object : ITGNativeCallback() {
        override fun onLoaded(nativeAdView: NativeAdView, nativeAd: NativeAd, placement: String) {
            val mediaContent = nativeAd.mediaContent

            if (mediaContent.hasVideoContent()) {
                // 🎬 Đây là VIDEO
                val duration = mediaContent.duration          // thời lượng (ms)
                val aspectRatio = mediaContent.aspectRatio    // tỷ lệ khung hình
                Log.d("Ads", "Video ad — duration: ${duration}ms, ratio: $aspectRatio")

                // Điều khiển video
                mediaContent.videoController?.let { controller ->
                    controller.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks {
                        override fun onVideoStart() { Log.d("Ads", "Video started") }
                        override fun onVideoEnd() { Log.d("Ads", "Video ended") }
                        override fun onVideoMute(isMuted: Boolean) { Log.d("Ads", "Muted: $isMuted") }
                    }
                }
            } else {
                // 🖼️ Đây là ẢNH (image/list ảnh)
                val aspectRatio = mediaContent.aspectRatio
                Log.d("Ads", "Image ad — ratio: $aspectRatio")
            }
        }
    }
)
```

#### Preload Native

```kotlin
ITGAdsNative.preload(
    placement = "NATIVE",
    layoutRes = R.layout.layout_native_large,
    callback = object : PreloadCallback {
        override fun onPreloadSuccess(placement: String) {
            Log.d("Ads", "Native preload success: $placement")
        }

        override fun onPreloadFailed(error: ITGNativeError) {
            Log.e("Ads", "Native preload failed: ${error.message}")
        }
    }
)
```

#### Quản lý Native

```kotlin
// Xóa cache của placement
ITGAdsNative.clearCache("NATIVE")

// Xóa tất cả cache
ITGAdsNative.clearCache()

// Ẩn native ad
ITGAdsNative.hideNativeAd(viewGroup = findViewById(R.id.nativeContainer))
```

#### Layout XML cho Native

```xml
<!-- Native Ad Container với shimmer loading -->
<FrameLayout
    android:id="@+id/nativeAdContainer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp">

    <include layout="@layout/ads_shimmer_native_large" />
</FrameLayout>
```

---

### Rewarded Ads

#### Preload

```kotlin
ITGAdsRewarded.preload(
    placement = "REWARDED",
    forceRefresh = false,
    callback = object : ITGRewardedCallback() {
        override fun onLoaded(placement: String) {
            Log.d("Ads", "Rewarded loaded")
        }

        override fun onFailed(placement: String, error: ITGRewardedError) {
            Log.e("Ads", "Rewarded failed: ${error.message}")
        }
    }
)
```

#### Show

```kotlin
ITGAdsRewarded.show(
    placement = "REWARDED",
    activity = this,
    forceShow = false,
    loadIfNotAvailable = true,
    callback = object : ITGRewardedCallback() {
        override fun onUserEarnedReward(placement: String, rewardType: String, rewardAmount: Int) {
            // 🎉 Người dùng đã xem xong — trao thưởng!
            Log.d("Ads", "Reward earned: $rewardType x $rewardAmount")
            grantReward(rewardType, rewardAmount)
        }

        override fun onDismissed(placement: String) {
            Log.d("Ads", "Rewarded dismissed")
        }

        override fun onFailed(placement: String, error: ITGRewardedError) {
            Log.e("Ads", "Show failed: ${error.message}")
        }

        override fun onImpression(placement: String) {
            Log.d("Ads", "Rewarded impression")
        }

        override fun onPaid(placement: String, revenue: AdRevenue) {
            Log.d("Ads", "Revenue: ${revenue.value}")
        }
    }
)
```

#### Kiểm tra trạng thái

```kotlin
val isLoaded = ITGAdsRewarded.isLoaded("REWARDED")
val isReady = ITGAdsRewarded.isReady("REWARDED")
ITGAdsRewarded.clearCache("REWARDED")
ITGAdsRewarded.clearCache() // xóa tất cả
```

---

### Rewarded Interstitial Ads

#### Preload

```kotlin
ITGAdsRewardedInter.preload(
    placement = "REWARD_INTER",
    forceRefresh = false,
    callback = object : ITGAdsRewardedInterCallback() {
        override fun onLoaded(placement: String) {
            Log.d("Ads", "Rewarded Inter loaded")
        }

        override fun onFailed(placement: String, error: ITGAdsRewardedInterError) {
            Log.e("Ads", "Failed: ${error.message}")
        }
    }
)
```

#### Show

```kotlin
ITGAdsRewardedInter.show(
    placement = "REWARD_INTER",
    activity = this,
    forceShow = false,
    loadIfNotAvailable = true,
    callback = object : ITGAdsRewardedInterCallback() {
        override fun onUserEarnedReward(placement: String, rewardType: String, rewardAmount: Int) {
            Log.d("Ads", "Reward: $rewardType x $rewardAmount")
        }

        override fun onDismissed(placement: String) {
            Log.d("Ads", "Dismissed")
        }

        override fun onFailed(placement: String, error: ITGAdsRewardedInterError) {
            Log.e("Ads", "Failed: ${error.message}")
        }
    }
)
```

---

### Open App Ads (Resume)

Tự động hiển thị quảng cáo Open App khi người dùng quay lại ứng dụng từ background:

#### Đăng ký (trong Application class)

```kotlin
ITGAdsSDK.onAdMobReady {
    // Đăng ký Open App Resume
    ITGAdsOpenResume.registerAndLoad(
        placement = "OPEN_RESUME",
        callback = object : ITGOpenCallback() {
            override fun onLoaded(placement: String) {
                Log.d("Ads", "Open resume loaded")
            }

            override fun onFailed(placement: String, error: ITGOpenError) {
                Log.e("Ads", "Open resume failed: ${error.message}")
            }
        }
    )
}
```

#### Quản lý Resume Ads

```kotlin
// Bỏ qua lần hiển thị tiếp theo (VD: khi user vừa mua hàng)
ITGAdsSDK.skipNextShow()

// Tắt hoàn toàn resume ads
ITGAdsSDK.disableOpenResume()

// Bật lại resume ads
ITGAdsSDK.enableOpenResume()

// Kiểm tra trạng thái
val isEnabled = ITGAdsSDK.isOpenResumeEnabled()

// Ignore resume ads cho một Activity cụ thể (VD: màn hình thanh toán)
ITGAdsSDK.setIgnoreAdResume(PaymentActivity::class.java)

// Gỡ ignore
ITGAdsSDK.removeIgnoreAdResume(PaymentActivity::class.java)
```

---

### Open App Ads (Splash)

Dùng cho **màn hình Splash** thay Interstitial Splash:

```kotlin
ITGAdsSDK.onAdMobReady(timeoutMs = 7000) {
    ITGAdsOpenSplash.show(
        placement = "OPEN_SPLASH",
        activity = this,
        timeoutMs = 15_000L,
        callback = object : ITGOpenCallback() {
            override fun onLoaded(placement: String) {
                Log.d("Ads", "Open splash loaded")
            }

            override fun onFailed(placement: String, error: ITGOpenError) {
                Log.e("Ads", "Open splash failed: ${error.message}")
            }
        },
        nextAction = {
            // Chuyển sang màn hình chính
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    )
}
```

---

## 🛡️ CMP (Consent Management)

SDK tự động xử lý consent theo **GDPR** thông qua Google UMP (User Messaging Platform).

#### Tự động (khuyến nghị)

Đặt `cmp_auto: true` trong `ad_config.json` — SDK sẽ tự hiển thị consent form khi cần.

#### Thủ công

```kotlin
// Yêu cầu consent thủ công
ITGAdsSDK.requestConsent(activity)

// Đăng ký callback khi consent hoàn tất
ITGAdsSDK.setConsentFlowCompletedCallback { canRequestAds ->
    if (canRequestAds) {
        Log.d("CMP", "User consented — can show ads")
    } else {
        Log.d("CMP", "User declined — limited ads")
    }
}
```

**CMP API nâng cao:**

```kotlin
val cmpManager = ITGAdsSDK.getCMPManager()

// Kiểm tra trạng thái
val canShowAds = cmpManager.canShowAds()
val canRequestAds = cmpManager.canRequestAds()
val isPrivacyRequired = cmpManager.isPrivacyOptionsRequired()
val status = cmpManager.getConsentStatus() // UNKNOWN, REQUIRED, NOT_REQUIRED, OBTAINED

// Hiển thị form privacy options (cho Settings screen)
cmpManager.showPrivacyOptionsForm(activity) {
    Log.d("CMP", "Privacy form dismissed")
}
```

---

## 🔩 Cấu hình nâng cao

### Bật/tắt loại quảng cáo

```kotlin
// Tắt một loại quảng cáo cụ thể
ITGAdsSDK.setAdTypeEnabled(ITGAdsType.BANNER, enabled = false)
ITGAdsSDK.setAdTypeEnabled(ITGAdsType.INTERSTITIAL, enabled = false)
ITGAdsSDK.setAdTypeEnabled(ITGAdsType.NATIVE, enabled = false)
ITGAdsSDK.setAdTypeEnabled(ITGAdsType.REWARD, enabled = false)
ITGAdsSDK.setAdTypeEnabled(ITGAdsType.REWARD_INTER, enabled = false)
ITGAdsSDK.setAdTypeEnabled(ITGAdsType.OPEN_APP, enabled = false)

// Tắt tất cả (VD: user đã mua premium)
ITGAdsSDK.setAdTypeDisabled()

// Kiểm tra trạng thái
val isBannerEnabled = ITGAdsSDK.isAdTypeEnabled(ITGAdsType.BANNER)
```

### Resume Mode

SDK hỗ trợ 2 chế độ resume ads:

```kotlin
// Chế độ Open App Ads khi resume
ITGAdsSDK.setResumeMode(ITGAdsSDK.ResumeMode.OPEN_ADS, "OPEN_RESUME")

// Chế độ Interstitial khi resume (kèm dialog "Welcome Back")
ITGAdsSDK.setResumeMode(ITGAdsSDK.ResumeMode.INTERSTITIAL, "INTER_RESUME")

// Tắt resume ads
ITGAdsSDK.setResumeMode(ITGAdsSDK.ResumeMode.NONE, "")
```

### Lắng nghe sự kiện quảng cáo toàn cục

```kotlin
ITGAdsSDK.setAdCallback { event ->
    when (event) {
        is AdEvent.Paid -> {
            Log.d("Revenue", "Ad revenue: ${event.adValue}")
        }
        // ... other events
    }
}
```

### `onAdMobReady` — Chờ SDK sẵn sàng

```kotlin
// Chờ mặc định 30s
ITGAdsSDK.onAdMobReady {
    // AdMob SDK đã sẵn sàng — tải quảng cáo
}

// Chờ tối đa 7s (dùng cho Splash)
ITGAdsSDK.onAdMobReady(timeoutMs = 7000) {
    // Load splash ads
}
```

---

## 📖 API Reference

### `ITGAdsSDK` — Core SDK

| Method | Mô tả |
|--------|--------|
| `initialize(context, config)` | Khởi tạo SDK |
| `onAdMobReady(timeoutMs, callback)` | Chờ AdMob sẵn sàng |
| `setAdCallback(listener)` | Đăng ký global ad listener |
| `setAdTypeEnabled(type, enabled)` | Bật/tắt loại quảng cáo |
| `setAdTypeDisabled()` | Tắt tất cả quảng cáo |
| `isAdTypeEnabled(type)` | Kiểm tra loại ad có enabled không |
| `isGlobalEnabled()` | Kiểm tra global enable |
| `setIgnoreAdResume(activityClass)` | Ignore resume ad cho Activity |
| `removeIgnoreAdResume(activityClass)` | Gỡ ignore |
| `enableOpenResume()` | Bật resume ads |
| `disableOpenResume()` | Tắt resume ads |
| `skipNextShow()` | Bỏ qua lần show resume tiếp |
| `setResumeMode(mode, placement)` | Đặt chế độ resume |
| `requestConsent(activity)` | Yêu cầu consent thủ công |
| `getConfigManager()` | Lấy config manager |
| `getCMPManager()` | Lấy CMP manager |
| `getEventBus()` | Lấy event bus |
| `getCurrentActivity()` | Lấy Activity hiện tại |

### `ITGAdsBanner` — Banner Ads

| Method | Mô tả |
|--------|--------|
| `showInlineAdaptive(placement, viewGroup, ...)` | Hiển thị inline adaptive banner |
| `showAnchoredAdaptive(placement, viewGroup, ...)` | Hiển thị anchored adaptive banner |
| `showCollapsible(placement, viewGroup, ...)` | Hiển thị collapsible banner |
| `show(placement, viewGroup, adSize, ...)` | Hiển thị fixed size banner |
| `preload(placement, ...)` | Tải trước banner |
| `clearCache(placement?)` | Xóa cache |
| `clearAllCache()` | Xóa toàn bộ cache |

### `ITGIAdsInter` — Interstitial Ads

| Method | Mô tả |
|--------|--------|
| `preload(placement, ...)` | Tải trước interstitial |
| `show(placement, activity, ...)` | Hiển thị interstitial |
| `isLoaded(placement)` | Kiểm tra đã load |
| `isReady(placement)` | Kiểm tra đã sẵn sàng |
| `clearCache(placement?)` | Xóa cache |

### `ITGAdsInterSplash` — Splash Interstitial

| Method | Mô tả |
|--------|--------|
| `show(placement, activity, ...)` | Hiển thị splash interstitial |
| `cancel()` | Hủy timer |
| `isLoaded(placement)` | Kiểm tra đã load |
| `clearCache(placement)` | Xóa cache |

### `ITGAdsNative` — Native Ads

| Method | Mô tả |
|--------|--------|
| `showSmall(placement, viewGroup, ...)` | Hiển thị native small |
| `showMedium(placement, viewGroup, ...)` | Hiển thị native medium |
| `showSmallAsBanner(placement, viewGroup, ...)` | Hiển thị native small as banner |
| `showLarge(placement, viewGroup, ...)` | Hiển thị native large |
| `showFullscreen(placement, viewGroup, ...)` | Hiển thị native fullscreen |
| `preload(placement, layoutRes, callback)` | Tải trước native |
| `clearCache(placement?)` | Xóa cache |
| `hideNativeAd(viewGroup)` | Ẩn native ad |

### `ITGAdsRewarded` — Rewarded Ads

| Method | Mô tả |
|--------|--------|
| `preload(placement, ...)` | Tải trước rewarded |
| `show(placement, activity, ...)` | Hiển thị rewarded |
| `isLoaded(placement)` | Kiểm tra đã load |
| `isReady(placement)` | Kiểm tra đã sẵn sàng |
| `clearCache(placement?)` | Xóa cache |

### `ITGAdsRewardedInter` — Rewarded Interstitial Ads

| Method | Mô tả |
|--------|--------|
| `preload(placement, ...)` | Tải trước |
| `show(placement, activity, ...)` | Hiển thị |
| `isLoaded(placement)` | Kiểm tra đã load |
| `isReady(placement)` | Kiểm tra đã sẵn sàng |
| `clearCache(placement?)` | Xóa cache |

### `ITGAdsOpenResume` — Open App Resume

| Method | Mô tả |
|--------|--------|
| `registerAndLoad(placement, callback)` | Đăng ký và tải |
| `unregister()` | Hủy đăng ký |
| `preload(placement, ...)` | Tải trước |
| `skipNextShow()` | Bỏ qua lần show tiếp |
| `clearCache(placement)` | Xóa cache |
| `clearAllCache()` | Xóa toàn bộ |

### `ITGAdsOpenSplash` — Open App Splash

| Method | Mô tả |
|--------|--------|
| `show(placement, activity, ..., nextAction)` | Hiển thị splash open ads |
| `cancel()` | Hủy timer |
| `isLoaded(placement)` | Kiểm tra đã load |

---

## 🏗️ Kiến trúc thư viện

```
mylibrary/
├── ads/                         # Public API — entry points
│   ├── ITGAdsSDK.kt             # Core SDK singleton
│   ├── ITGAdsSDKConfig.kt       # SDK configuration data class
│   ├── ITGAdsBanner.kt          # Banner ads API
│   ├── ITGIAdsInter.kt          # Interstitial ads API
│   ├── ITGAdsInterSplash.kt     # Splash interstitial API
│   ├── ITGAdsInterResume.kt     # Interstitial resume API
│   ├── ITGAdsOpenResume.kt      # Open app resume API
│   ├── ITGAdsOpenSplash.kt      # Open app splash API
│   └── ITGAdsRewardedInter.kt   # Rewarded interstitial API
│
├── banner/                      # Banner internal implementation
│   ├── ITGBannerLoader.kt       # Banner loading logic
│   ├── ITGBannerCache.kt        # Banner caching
│   ├── ITGBannerEntry.kt        # Banner cache entry
│   ├── ITGBannerType.kt         # Banner type enum
│   ├── ITGAnchorPosition.kt     # Anchor position enum
│   └── ITGCollapsiblePosition.kt # Collapsible position enum
│
├── inter/                       # Interstitial internal
│   ├── ITGInterLoader.kt        # Interstitial loading logic
│   └── ITGInterCache.kt         # Interstitial caching
│
├── native/                      # Native internal
│   ├── ITGAdsNative.kt          # Native ads API
│   ├── ITGNativeLoader.kt       # Native loading logic
│   └── ITGNativeCache.kt        # Native caching
│
├── rewarded/                    # Rewarded internal
│   ├── ITGAdsRewarded.kt        # Rewarded ads API
│   └── ITGRewardedLoader.kt     # Rewarded loading logic
│
├── rewarded_inter/              # Rewarded interstitial internal
│   └── ITGAdsRewardedInterLoader.kt
│
├── open/                        # Open app internal
│   └── ITGOpenLoader.kt         # Open app loading logic
│
├── callback/                    # Callback interfaces
│   ├── ITGBannerCallback.kt
│   ├── ITGInterCallback.kt
│   ├── ITGNativeCallback.kt
│   ├── ITGOpenCallback.kt
│   └── ITGRewardedCallback.kt
│
├── config/                      # Configuration management
│   ├── ITGConfigManager.kt      # Config interface
│   ├── ITGConfigManagerImpl.kt  # Config implementation
│   ├── ITGAdConfig.kt           # Config model
│   ├── ITGAdPlacementConfig.kt  # Placement config model
│   ├── ITGGlobalConfig.kt       # Global config model
│   └── ITGAdsType.kt            # Ad type enum
│
├── cmp/                         # Consent Management
│   ├── ITGCMPManager.kt         # CMP interface
│   └── ITGCMPManagerImpl.kt     # CMP implementation
│
├── core/                        # Core utilities
│   ├── AdPlacementThreadDispatcher.kt  # Thread management
│   ├── SharePreferenceUtils.kt         # SharedPreferences helper
│   └── event/                          # Event system
│       ├── EventBus.kt
│       ├── AdEvent.kt
│       └── AdEventListener.kt
│
├── revenue/                     # Revenue tracking
│   └── AdRevenue.kt
│
└── dialog/                      # UI components
    └── DialogWelcomeBackAds.kt  # Welcome back dialog
```

---

## ❓ FAQ & Troubleshooting

### Q: Quảng cáo không hiển thị?

1. Kiểm tra đã gọi `ITGAdsSDK.initialize()` trong `Application.onCreate()` chưa
2. Kiểm tra placement name trong code khớp với `ad_config.json`
3. Kiểm tra `enable: true` trong config
4. Sử dụng `ITGAdsSDK.onAdMobReady { }` trước khi load quảng cáo
5. Kiểm tra log với tag `ITGAdsSDK` trong Logcat

### Q: Resume ads không hiển thị khi quay lại app?

1. Kiểm tra đã gọi `registerAndLoad()` chưa
2. Kiểm tra `isOpenResumeEnabled()` = true
3. Kiểm tra Activity hiện tại không nằm trong ignore list
4. Kiểm tra không có fullscreen ad đang hiển thị (`isShowingFullScreen()`)

### Q: Làm sao tắt quảng cáo cho user Premium?

```kotlin
// Cách 1: Tắt tất cả loại quảng cáo
ITGAdsSDK.setAdTypeDisabled()

// Cách 2: Hủy đăng ký resume ads
ITGAdsOpenResume.unregister()
```

### Q: Làm sao đổi config từ Firebase Remote Config?

Upload JSON với cùng cấu trúc `ad_config.json` lên Firebase Remote Config. SDK sẽ tự động merge config remote với config local, ưu tiên remote.

### Q: Tại sao cần gọi `onAdMobReady`?

SDK khởi tạo AdMob **bất đồng bộ** sau khi CMP consent hoàn tất. `onAdMobReady` đảm bảo bạn chỉ load quảng cáo khi SDK đã sẵn sàng.

---

## 📝 License

Copyright © 2026 ITG team. All rights reserved.
