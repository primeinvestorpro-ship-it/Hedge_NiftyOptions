package com.hedgenifty.data

import com.google.gson.annotations.SerializedName
import com.hedgenifty.models.TradeType
import com.hedgenifty.models.TradeStatus
import com.hedgenifty.models.TradeRecord

// --- NSE API Models ---

data class NSEOptionChainResponse(
    @SerializedName("records") val records: NSERecords,
    @SerializedName("filtered") val filtered: NSEFiltered
)

data class NSERecords(
    @SerializedName("expiryDates") val expiryDates: List<String>,
    @SerializedName("data") val data: List<NSEMarketData>,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("underlyingValue") val underlyingValue: Double,
    @SerializedName("strikePrices") val strikePrices: List<Double>
)

data class NSEFiltered(
    @SerializedName("data") val data: List<NSEMarketData>,
    @SerializedName("CE") val ceTotal: NSETotalInfo,
    @SerializedName("PE") val peTotal: NSETotalInfo
)

data class NSEMarketData(
    @SerializedName("strikePrice") val strikePrice: Double,
    @SerializedName("expiryDate") val expiryDate: String,
    @SerializedName("CE") val call: NSEOptionInfo?,
    @SerializedName("PE") val put: NSEOptionInfo?
)

data class NSEOptionInfo(
    @SerializedName("strikePrice") val strikePrice: Double,
    @SerializedName("expiryDate") val expiryDate: String,
    @SerializedName("openInterest") val openInterest: Double,
    @SerializedName("changeinOpenInterest") val changeInOI: Double,
    @SerializedName("pchangeinOpenInterest") val pChangeInOI: Double,
    @SerializedName("totalTradedVolume") val volume: Long,
    @SerializedName("impliedVolatility") val iv: Double,
    @SerializedName("lastPrice") val ltp: Double,
    @SerializedName("change") val change: Double,
    @SerializedName("pChange") val pChange: Double,
    @SerializedName("totalBuyQuantity") val totalBuyQty: Long,
    @SerializedName("totalSellQuantity") val totalSellQty: Long,
    @SerializedName("bidQty") val bidQty: Long,
    @SerializedName("bidprice") val bidPrice: Double,
    @SerializedName("askQty") val askQty: Long,
    @SerializedName("askPrice") val askPrice: Double,
    @SerializedName("underlyingValue") val underlyingValue: Double
)

data class NSETotalInfo(
    @SerializedName("totOI") val totalOI: Long,
    @SerializedName("totVol") val totalVol: Long
)

// --- Internal App Models (Processed from NSE Data) ---

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
