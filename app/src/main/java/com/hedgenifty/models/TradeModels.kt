package com.hedgenifty.models

data class TradeSignal(
    val type: TradeType,
    val strikePrice: Double,
    val quantity: Int,
    val entryPrice: Double,
    val stopLoss: Double,
    val target: Double,
    val timestamp: Long
)

data class TradeRecord(
    val id: String,
    val tradeSignal: TradeSignal,
    val status: TradeStatus,
    val executedPrice: Double,
    val exitPrice: Double,
    val profitLoss: Double,
    val closedAt: Long?
)

enum class TradeType {
    CALL,
    PUT,
    STRADDLE
}

enum class TradeStatus {
    PENDING,
    EXECUTED,
    PARTIALLY_FILLED,
    CANCELLED,
    CLOSED,
    FAILED
}