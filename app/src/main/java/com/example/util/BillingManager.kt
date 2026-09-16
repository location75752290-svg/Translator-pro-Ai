package com.example.util

import android.content.Context

object BillingManager {
    const val FREE_CERT_LIMIT = 1

    // SharedPreferences se check karo
    fun canGenerateCertificate(context: Context): Boolean {
        val prefs = context.getSharedPreferences("billing", Context.MODE_PRIVATE)
        val count = prefs.getInt("cert_count", 0)
        val isPro = prefs.getBoolean("is_pro", false)
        return isPro || count < FREE_CERT_LIMIT
    }

    fun incrementCertCount(context: Context) {
        val prefs = context.getSharedPreferences("billing", Context.MODE_PRIVATE)
        prefs.edit().putInt("cert_count", prefs.getInt("cert_count", 0) + 1).apply()
    }
    
    fun unlockPro(context: Context) {
        context.getSharedPreferences("billing", Context.MODE_PRIVATE).edit().putBoolean("is_pro", true).apply()
    }

    fun isPro(context: Context): Boolean {
        val prefs = context.getSharedPreferences("billing", Context.MODE_PRIVATE)
        return prefs.getBoolean("is_pro", false)
    }

    fun getCertCount(context: Context): Int {
        val prefs = context.getSharedPreferences("billing", Context.MODE_PRIVATE)
        return prefs.getInt("cert_count", 0)
    }
}
