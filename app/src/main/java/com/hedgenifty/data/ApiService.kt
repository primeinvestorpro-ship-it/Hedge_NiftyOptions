package com.hedgenifty.data

import com.hedgenifty.models.TradeSignal
import com.hedgenifty.models.TradeRecord
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body

interface ApiService {
    @GET("fetchNSEData")
    fun getNiftyData(@Query("symbol") symbol: String): Call<NiftyData>
    
    @GET("getOptionChain")
    fun getOptionsChain(@Query("symbol") symbol: String): Call<List<OptionChainData>>
    
    @GET("calculateStrategy")
    fun calculateStrategy(
        @Query("spotPrice") spotPrice: Double,
        @Query("expiryDate") expiryDate: String
    ): Call<StrategyResult>
    
    @POST("triggerTrade")
    fun triggerTrade(@Body tradeSignal: TradeSignal): Call<Boolean>
    
    @GET("getTradeHistory")
    fun getTradeHistory(): Call<List<TradeRecord>>
}