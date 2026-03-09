package com.admob.adsnextgen

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.admob.next.gen.ads.ITGAdsInterSplash
import com.admob.next.gen.ads.ITGAdsSDK
import com.admob.next.gen.callback.ITGInterCallback
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Timber.tag("TAG").d("onCreate1: ${System.currentTimeMillis()}")
        ITGAdsSDK.onAdMobReady(7000) {
            Timber.tag("TAG").d("onCreate2: ${System.currentTimeMillis()}")
            ITGAdsInterSplash.show(
                "INTERSTITIAL_SPLASH",
                this,
                showLoading = true,
                showAdCallback = object : ITGInterCallback() {
                    override fun onDismissed(placement: String) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
            )
        }
    }
}