package com.admob.adsnextgen

import ads_mobile_sdk.tr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.admob.next.gen.ads.ITGAdsBanner
import com.admob.next.gen.ads.ITGIAdsInter
import com.admob.next.gen.banner.ITGBannerError
import com.admob.next.gen.callback.ITGBannerCallback
import com.admob.next.gen.callback.ITGInterCallback
import com.admob.next.gen.callback.ITGNativeCallback
import com.admob.next.gen.callback.ITGRewardedCallback
import com.admob.next.gen.inter.ITGInterError
import com.admob.next.gen.native.ITGAdsNative
import com.admob.next.gen.rewarded.ITGAdsRewarded
import com.admob.next.gen.rewarded.ITGRewardedError
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Banner Normal
        /*ITGAdsBanner.showAnchoredAdaptive(
            placement = "BANNER",
            viewGroup = findViewById(R.id.banner),
            forceRefresh = true,
            center = true,
            callback = object : ITGBannerCallback() {
                override fun onLoaded(
                    adView: AdView,
                    placement: String
                ) {
                    Log.d(TAG, "Banner loaded for placement: $placement")
                }

                override fun onFailed(placement: String, error: ITGBannerError) {
                    Log.e(TAG, "Banner failed: $error")
                }
            })*/

        // Banner Collapsible
        ITGAdsBanner.showCollapsible(
            placement = "BANNER_COLLAPSIBLE",
            viewGroup = findViewById(R.id.banner),
            callback = object : ITGBannerCallback() {
                override fun onLoaded(
                    adView: AdView,
                    placement: String
                ) {
                    Timber.tag(TAG).d("Banner loaded for placement: $placement")
                }

                override fun onFailed(placement: String, error: ITGBannerError) {
                    Timber.tag(TAG).e("Banner failed: $error")
                }
            })

        // Native
        // Load And Show
        ITGAdsNative.showLarge(
            placement = "NATIVE",
            viewGroup = findViewById(R.id.nativeAdContainer),
            layoutRes = R.layout.layout_native_large,
            shimmerLayoutRes = R.layout.ads_shimmer_native_large,
            callback = object : ITGNativeCallback() {

            })

        // Preload Native
        /*ITGAdsNative.preload(
            placement = "NATIVE",
            layoutRes = R.layout.layout_native_large,
            callback = object : PreloadCallback {
                override fun onPreloadSuccess(placement: String) {
                    Log.d(TAG, "Native preload success for placement: $placement")
                }

                override fun onPreloadFailed(error: ITGNativeError) {
                    Log.e(TAG, "Native preload failed: $error")
                }
            })*/

        // Clear Native Cache
        //ITGAdsNative.clearCache()

        // Clear Native Cache For Placement
        //ITGAdsNative.clearCache("NATIVE")


        // Inter
        actionInter()

        // Rewarded
        actionRewarded()
    }

    /**
     * Inter
     */

    private fun actionInter() {
        val btnLoad: Button = findViewById(R.id.btnLoadInter)
        val btnShow: Button = findViewById(R.id.btnShowInter)
        btnLoad.setOnClickListener {
            loadInterForPlacement()
        }
        btnShow.setOnClickListener {
            showInterForPlacement()
        }
    }

    private fun loadInterForPlacement() {
        ITGIAdsInter.preload(
            placement = "INTERSTITIAL_ALL",
            callback = object : ITGInterCallback() {
                override fun onLoaded(placement: String) {
                    super.onLoaded(placement)
                    Toast.makeText(this@MainActivity, "Inter loaded for placement: $placement", Toast.LENGTH_SHORT).show()
                    Timber.tag(TAG).d("Inter loaded for placement: $placement")
                }

                override fun onFailed(placement: String, error: ITGInterError) {
                    super.onFailed(placement, error)
                    Toast.makeText(this@MainActivity, "Inter failed: $error", Toast.LENGTH_SHORT).show()
                    Timber.tag(TAG).e("Inter failed: $error")
                }
            })
    }

    private fun showInterForPlacement() {
        ITGIAdsInter.show(
            placement = "INTERSTITIAL_ALL",
            activity = this,
            callback = object : ITGInterCallback() {
                override fun onDismissed(placement: String) {
                    super.onDismissed(placement)
                    startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                }
            })

    }

    /**
     * Rewarded
     */

    private fun actionRewarded() {
        val btnLoad: Button = findViewById(R.id.btnLoadReward)
        val btnShow: Button = findViewById(R.id.btnShowRewarded)
        btnLoad.setOnClickListener {
            loadRewardedForPlacement()
        }

        btnShow.setOnClickListener {
            showRewardedForPlacement()
        }
    }

    private fun loadRewardedForPlacement() {
        ITGAdsRewarded.preload(
            placement = "REWARDED",
            callback = object : ITGRewardedCallback() {
                override fun onLoaded(placement: String) {
                    super.onLoaded(placement)
                    Toast.makeText(this@MainActivity, "Rewarded loaded for placement: $placement", Toast.LENGTH_SHORT).show()
                    Timber.tag(TAG).d("Rewarded loaded for placement: $placement")
                }

                override fun onFailed(placement: String, error: ITGRewardedError) {
                    super.onFailed(placement, error)
                    Toast.makeText(this@MainActivity, "Rewarded failed: $error", Toast.LENGTH_SHORT).show()
                    Timber.tag(TAG).e("Rewarded failed: $error")
                }
            })
    }

    private fun showRewardedForPlacement() {
        ITGAdsRewarded.show(
            placement = "REWARDED",
            activity = this,
            callback = object : ITGRewardedCallback() {
                override fun onDismissed(placement: String) {
                    super.onDismissed(placement)
                    startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                }
            })
    }
}