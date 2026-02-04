package com.hedgenifty.data

import com.hedgenifty.models.TradeSignal
import com.hedgenifty.models.TradeRecord

interface OptionsRepository {
    suspend fun getLiveNiftyData(): NiftyData?
    suspend fun getOptionsChain(): List<OptionChainData>?
    suspend fun calculateStrategy(niftyPrice: Double, expiryDate: String): StrategyResult?
    suspend fun triggerTrade(tradeSignal: TradeSignal): Boolean
    suspend fun getTradeHistory(): List<TradeRecord>?
    fun startLiveUpdates(callback: (NiftyData) -> Unit)
    fun stopLiveUpdates()
}

data class NiftyData(
    val spotPrice: Double,
    val expiryDate: String,
    val currentIV: Double,
    val atmStrike: Double,
    val callPrice: Double,
    val putPrice: Double,
    val straddlePrice: Double,
    val theta: Double,
    val gamma: Double,
    val oiChange: Double,
    val volume: Double,
    val bidAskSpread: Double,
    val timestamp: Long
)

data class OptionChainData(
    val strikePrice: Double,
    val callOI: Double,
    val putOI: Double,
    val callVolume: Double,
    val putVolume: Double,
    val callIV: Double,
    val putIV: Double,
    val callBid: Double,
    val callAsk: Double,
    val putBid: Double,
    val putAsk: Double,
    val timestamp: Long
)

data class StrategyResult(
    val edgeScores: EdgeScores,
    val probabilityExpectancy: ProbabilityExpectancy,
    val regimeProbs: RegimeProbs,
    val tradeDecision: TradeDecision,
    val riskMetrics: RiskMetrics,
    val filters: Filters,
    val timestamp: Long
)

data class EdgeScores(
    val callEdge: Int,
    val putEdge: Int,
    val straddleEdge: Int
)

data class ProbabilityExpectancy(
    val winProbability: Double,
    val requiredMove: Double,
    val timeWindow: Int,
    val expectedMove: Double,
    val netExpectancy: Double
)

data class RegimeProbs(
    val slowRange: Double,
    val compressionExpansion: Double,
    val fastTrend: Double,
    val erraticChop: Double,
    val next30_90Shift: Map<String, Double>
)

data class TradeDecision(
    val decision: String,
    val strike: Double,
    val entryWindow: Int,
    val maxHoldingTime: Int,
    val positionSize: Double,
    val stopLoss: Double,
    val profitTarget: Double
)

data class RiskMetrics(
    val thetaBurn15min: Double,
    val ivExpansionProb: Double,
    val ivCrushRisk: Double,
    val biggestInvalidation: String
)

data class Filters(
    val ivVelocityFilter: Boolean,
    val straddleMathFilter: Boolean,
    val oiMicroFilter: Boolean,
    val gammaAmplificationFilter: Boolean,
    val microstructureFilter: Boolean,
    val timeCutoffFilter: Boolean
)