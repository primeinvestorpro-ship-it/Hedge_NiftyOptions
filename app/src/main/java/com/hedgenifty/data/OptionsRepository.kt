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
