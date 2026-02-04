package com.hedgenifty.utils

import android.content.Context
import android.util.Log
import com.zerodha.kiteconnect.KiteConnect
import com.zerodha.kiteconnect.KiteException
import com.zerodha.kiteconnect.kiteconnect.models.Order
import com.zerodha.kiteconnect.kiteconnect.models.OrderParams
import com.zerodha.kiteconnect.kiteconnect.models.Position
import com.hedgenifty.data.TradeSignal
import com.hedgenifty.data.TradeRecord
import com.hedgenifty.models.TradeType
import com.hedgenifty.models.TradeStatus

class ZerodhaManager(private val context: Context) {
    private val kiteConnect: KiteConnect
    
    init {
        // Initialize with your API key
        kiteConnect = KiteConnect("your_api_key")
    }
    
    fun setAccessToken(accessToken: String) {
        kiteConnect.setAccessToken(accessToken)
    }
    
    fun getLoginURL(): String {
        return kiteConnect.getLoginURL()
    }
    
    fun generateSession(requestToken: String, apiSecret: String): String? {
        return try {
            val session = kiteConnect.generateSession(requestToken, apiSecret)
            session.accessToken
        } catch (e: KiteException) {
            Log.e("Zerodha", "Session generation failed: ${e.message}")
            null
        }
    }
    
    fun executeTrade(tradeSignal: TradeSignal, callback: (Boolean, String?) -> Unit) {
        val orderParams = createOrderParams(tradeSignal)
        
        try {
            val orderId = kiteConnect.placeOrder(orderParams)
            callback(true, orderId)
            
            // Save to trade history
            saveToTradeHistory(tradeSignal, orderId)
            
        } catch (e: KiteException) {
            Log.e("Zerodha", "Order placement failed: ${e.message}")
            callback(false, e.message)
        }
    }
    
    private fun createOrderParams(tradeSignal: TradeSignal): OrderParams {
        val orderParams = OrderParams()
        
        orderParams.variety = "bo" // Bracket Order
        orderParams.tradingsymbol = getTradingSymbol(tradeSignal)
        orderParams.exchange = "NFO"
        orderParams.transactionType = if (tradeSignal.type == TradeType.CALL || tradeSignal.type == TradeType.STRADDLE) "BUY" else "SELL"
        orderParams.quantity = tradeSignal.quantity.toString()
        orderParams.price = "0" // Market order
        orderParams.product = "MIS" // Margin Intraday Square off
        orderParams.orderType = "MARKET"
        orderParams.stoploss = calculateStopLoss(tradeSignal)
        orderParams.squareoff = calculateTarget(tradeSignal)
        
        return orderParams
    }
    
    private fun getTradingSymbol(tradeSignal: TradeSignal): String {
        val expiry = getNearestExpiry() // Implement expiry logic
        val strike = tradeSignal.strikePrice.toInt()
        
        return when(tradeSignal.type) {
            TradeType.CALL -> "NFO:NIFTY$expiry${strike}CE"
            TradeType.PUT -> "NFO:NIFTY$expiry${strike}PE"
            TradeType.STRADDLE -> "NFO:NIFTY$expiry${strike}CE" // Handle straddle separately
        }
    }
    
    private fun calculateStopLoss(tradeSignal: TradeSignal): String {
        return (tradeSignal.entryPrice * 0.98).toString() // 2% stop loss
    }
    
    private fun calculateTarget(tradeSignal: TradeSignal): String {
        return (tradeSignal.entryPrice * 1.02).toString() // 2% target
    }
    
    private fun saveToTradeHistory(tradeSignal: TradeSignal, orderId: String) {
        val record = TradeRecord(
            id = orderId,
            tradeSignal = tradeSignal,
            status = TradeStatus.EXECUTED,
            executedPrice = tradeSignal.entryPrice,
            exitPrice = 0.0,
            profitLoss = 0.0,
            closedAt = null
        )
        
        // Save to database or shared preferences
        val prefs = context.getSharedPreferences("HedgeNiftyPrefs", Context.MODE_PRIVATE)
        val json = com.google.gson.Gson().toJson(record)
        prefs.edit().putString("trade_record_${record.id}", json).apply()
    }
    
    fun getPositions(): List<Position>? {
        return try {
            kiteConnect.getPositions()
        } catch (e: KiteException) {
            Log.e("Zerodha", "Failed to get positions: ${e.message}")
            null
        }
    }
    
    fun getOrderHistory(orderId: String): Order? {
        return try {
            val orders = kiteConnect.getOrderHistory(orderId)
            orders.firstOrNull()
        } catch (e: KiteException) {
            Log.e("Zerodha", "Failed to get order history: ${e.message}")
            null
        }
    }
    
    private fun getNearestExpiry(): String {
        // Implement logic to get nearest weekly expiry
        // For now, return a default expiry
        return "26APR"
    }
}