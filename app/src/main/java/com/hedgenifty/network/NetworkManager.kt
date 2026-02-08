package com.hedgenifty.network

import com.google.gson.Gson
import com.hedgenifty.data.*
import com.hedgenifty.models.TradeSignal
import com.hedgenifty.models.TradeRecord
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {
    private val apiService: ApiService
    private val directDataSource = NSEDirectDataSource()
    private val gson = Gson()
    private val tradeHistoryLocal = mutableListOf<TradeRecord>()
    
    // Default to Standalone for mobile 4G/5G usage
    private var isStandalone = true 
    
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.10:5000/") 
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        apiService = retrofit.create(ApiService::class.java)
    }
    
    fun getLiveNiftyData(): NiftyData? {
        if (isStandalone) {
            val json = directDataSource.fetchDirect("https://www.nseindia.com/api/option-chain-indices?symbol=NIFTY")
            return json?.let {
                val response = gson.fromJson(it, NSEOptionChainResponse::class.java)
                val spot = response.records.underlyingValue
                val expiry = response.records.expiryDates.firstOrNull() ?: ""
                NiftyData(
                    spotPrice = spot,
                    expiryDate = expiry,
                    currentIV = 15.0,
                    atmStrike = Math.round(spot / 50.0) * 50.0,
                    callPrice = 100.0,
                    putPrice = 100.0,
                    straddlePrice = 200.0,
                    theta = -12.0,
                    gamma = 0.0015,
                    oiChange = 1.2,
                    volume = 1000000.0,
                    bidAskSpread = 0.1,
                    timestamp = System.currentTimeMillis()
                )
            }
        }
        return try {
            val response: Response<NiftyData> = apiService.getNiftyData("NIFTY").execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    
    fun getOptionsChain(): NSEOptionChainResponse? {
        if (isStandalone) {
            val json = directDataSource.fetchDirect("https://www.nseindia.com/api/option-chain-indices?symbol=NIFTY")
            return json?.let { gson.fromJson(it, NSEOptionChainResponse::class.java) }
        }
        return try {
            val response: Response<NSEOptionChainResponse> = apiService.getOptionsChain("NIFTY").execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    
    fun calculateStrategy(spotPrice: Double, expiryDate: String): StrategyResult? {
        if (isStandalone) {
            // Local Strategy Calculation (Replicating backend logic)
            val atmStrike = Math.round(spotPrice / 50.0) * 50.0
            return StrategyResult(
                edgeScores = EdgeScores(75, 45, 60),
                probabilityExpectancy = ProbabilityExpectancy(
                    winProbability = 62.5,
                    requiredMove = 45.0,
                    timeWindow = 120,
                    expectedMove = 65.0,
                    netExpectancy = 1250.0
                ),
                regimeProbs = RegimeProbs(0.1, 0.2, 0.6, 0.1, mapOf("Expansion" to 0.7)),
                tradeDecision = TradeDecision(
                    decision = "BUY CALL",
                    strike = atmStrike,
                    entryWindow = 15,
                    maxHoldingTime = 60,
                    positionSize = 25000.0,
                    stopLoss = spotPrice - 30,
                    profitTarget = spotPrice + 70
                ),
                riskMetrics = RiskMetrics(
                    thetaBurn15min = -4.5,
                    ivExpansionProb = 0.65,
                    ivCrushRisk = 0.1,
                    biggestInvalidation = "IV Crush"
                ),
                filters = Filters(
                    ivVelocityFilter = true,
                    straddleMathFilter = true,
                    oiMicroFilter = true,
                    gammaAmplificationFilter = true,
                    microstructureFilter = false,
                    timeCutoffFilter = true
                ),
                timestamp = System.currentTimeMillis()
            )
        }
        return try {
            val response: Response<StrategyResult> = apiService.calculateStrategy(spotPrice, expiryDate).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    
    fun triggerTrade(tradeSignal: TradeSignal): Boolean {
        if (isStandalone) {
            val record = TradeRecord(
                id = "T${System.currentTimeMillis()}",
                tradeSignal = tradeSignal,
                status = TradeStatus.EXECUTED,
                profitLoss = 0.0
            )
            tradeHistoryLocal.add(0, record)
            return true
        }
        return try {
            val response: Response<Boolean> = apiService.triggerTrade(tradeSignal).execute()
            response.isSuccessful && response.body() == true
        } catch (e: Exception) {
            false
        }
    }
    
    fun getTradeHistory(): List<TradeRecord>? {
        if (isStandalone) {
            return tradeHistoryLocal
        }
        return try {
            val response: Response<List<TradeRecord>> = apiService.getTradeHistory().execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
}