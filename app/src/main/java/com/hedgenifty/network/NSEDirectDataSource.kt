package com.hedgenifty.network

import android.util.Log
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class NSEDirectDataSource {
    private val client: OkHttpClient
    private val cookieJar: PersistentCookieJar = PersistentCookieJar()

    init {
        client = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val commonHeaders = Headers.Builder()
        .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
        .add("Accept", "*/*")
        .add("Accept-Language", "en-US,en;q=0.9")
        .add("Referer", "https://www.nseindia.com/")
        .add("Connection", "keep-alive")
        .build()

    /**
     * Replicates the nsepython "cookie dance": visits home page to get session cookies,
     * then executes the actual API call.
     */
    fun fetchDirect(url: String): String? {
        // Step 1: Visit home page if we have no cookies
        if (cookieJar.isEmpty()) {
            val homeRequest = Request.Builder()
                .url("https://www.nseindia.com/")
                .headers(commonHeaders)
                .build()
            
            try {
                client.newCall(homeRequest).execute().close()
                Log.d("NSEDirect", "Home page visited, cookies captured.")
            } catch (e: Exception) {
                Log.e("NSEDirect", "Failed to get cookies: ${e.message}")
                return null
            }
        }

        // Step 2: Call the actual API
        val apiRequest = Request.Builder()
            .url(url)
            .headers(commonHeaders)
            .build()

        return try {
            val response = client.newCall(apiRequest).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                response.close()
                body
            } else {
                Log.e("NSEDirect", "API Call failed: ${response.code}")
                response.close()
                null
            }
        } catch (e: Exception) {
            Log.e("NSEDirect", "Network error: ${e.message}")
            null
        }
    }

    // Simple in-memory cookie storer
    private class PersistentCookieJar : CookieJar {
        private val storage = mutableListOf<Cookie>()
        
        fun isEmpty() = storage.isEmpty()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            storage.addAll(cookies)
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            // Filter out expired cookies if necessary
            return storage.filter { it.expiresAt > System.currentTimeMillis() }
        }
    }
}
