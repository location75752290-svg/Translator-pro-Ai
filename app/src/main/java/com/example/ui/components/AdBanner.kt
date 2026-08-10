package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

object AdMobConfig {
    // Real AdMob Credentials
    const val REAL_APP_ID = "ca-app-pub-4296934840684193~8725700613"
    const val REAL_BANNER_AD_UNIT_ID = "ca-app-pub-4296934840684193/6930503293"

    // Test AdMob Credentials
    const val TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741"

    // Uses Test Ads in DEBUG mode, Real Ads in RELEASE mode
    val BANNER_AD_UNIT_ID: String
        get() = if (BuildConfig.DEBUG) TEST_BANNER_AD_UNIT_ID else REAL_BANNER_AD_UNIT_ID
}

@Composable
fun AdBanner(
    modifier: Modifier = Modifier,
    adUnitId: String = AdMobConfig.BANNER_AD_UNIT_ID
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(vertical = 4.dp)
            .testTag("admob_banner_ad"),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    setAdUnitId(adUnitId)
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
