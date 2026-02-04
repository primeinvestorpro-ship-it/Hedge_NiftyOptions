package com.hedgenifty.network

import com.hedgenifty.data.ApiService
import com.hedgenifty.data.NiftyData
import com.hedgenifty.data.OptionChainData
import com.hedgenifty.data.StrategyResult
import com.hedgenifty.models.TradeSignal
import com.hedgenifty.models.TradeRecord
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {
    private val apiService: ApiService
    
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        apiService = retrofit.create(ApiService::class.java)
    }
    
    fun getLiveNiftyData(): NiftyData? {
        return try {
            val response: Response<NiftyData> = apiService.getNiftyData("NIFTY").execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    
    fun getOptionsChain(): List<OptionChainData>? {
        return try {
            val response: Response<List<OptionChainData>> = apiService.getOptionsChain("NIFTY").execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    
    fun calculateStrategy(spotPrice: Double, expiryDate: String): StrategyResult? {
        return try {
            val response: Response<StrategyResult> = apiService.calculateStrategy(spotPrice, expiryDate).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    
    fun triggerTrade(tradeSignal: TradeSignal): Boolean {
        return try {
            val response: Response<Boolean> = apiService.triggerTrade(tradeSignal).execute()
            response.isSuccessful && response.body() == true
        } catch (e: Exception) {
            false
        }
    }
    
    fun getTradeHistory(): List<TradeRecord>? {
        return try {
            val response: Response<List<TradeRecord>> = apiService.getTradeHistory().execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
}