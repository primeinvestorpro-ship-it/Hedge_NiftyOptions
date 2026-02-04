package com.hedgenifty.network

import com.hedgenifty.data.ApiService
import com.hedgenifty.data.NiftyData
import com.hedgenifty.data.OptionChainData
import com.hedgenifty.data.StrategyResult
import com.hedgenifty.data.TradeSignal
import com.hedgenifty.data.TradeRecord
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkManager {
    private val apiService: ApiService
    
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        apiService = retrofit.create(ApiService::class.java)
    }
    
    suspend fun getLiveNiftyData(): NiftyData? {
        return try {
            val response = apiService.getNiftyData("NIFTY")
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getOptionsChain(): List<OptionChainData>? {
        return try {
            val response = apiService.getOptionsChain("NIFTY")
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun calculateStrategy(spotPrice: Double, expiryDate: String): StrategyResult? {
        return try {
            val response = apiService.calculateStrategy(spotPrice, expiryDate)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun triggerTrade(tradeSignal: TradeSignal): Boolean {
        return try {
            val response = apiService.triggerTrade(tradeSignal)
            response.isSuccessful && response.body() == true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun getTradeHistory(): List<TradeRecord>? {
        return try {
            val response = apiService.getTradeHistory()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
}