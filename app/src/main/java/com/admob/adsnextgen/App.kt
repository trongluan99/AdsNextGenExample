package com.admob.adsnextgen

import android.app.Application
import com.admob.next.gen.ads.ITGAdsOpenResume
import com.admob.next.gen.ads.ITGAdsSDK
import com.admob.next.gen.ads.ITGAdsSDKConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = ITGAdsSDKConfig(
            debug = false,
            testDevices = emptyList(),
            minInterval = 30L,
            appId = "ca-app-pub-3940256099942544~3347511713",
            adjustToken = "abc123",
            adjustTokenTiktok = "bcd123",
            facebookId = "1234566",
            facebookClientToken = "13463474",
        )

        ITGAdsSDK.initialize(this, config)
        ITGAdsSDK.onAdMobReady {
            ITGAdsOpenResume.registerAndLoad("OPEN_RESUME", null)
        }
    }
}