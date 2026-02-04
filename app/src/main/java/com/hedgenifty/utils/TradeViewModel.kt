package com.hedgenifty.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hedgenifty.models.TradeSignal
import com.hedgenifty.models.TradeRecord

class TradeViewModel(private val context: Context) : ViewModel() {
    private val prefs: SharedPreferences = context.getSharedPreferences("HedgeNiftyPrefs", Context.MODE_PRIVATE)
    
    val niftyPrice = mutableStateOf(0.0)
    val expiryDate = mutableStateOf("")
    val atmStrike = mutableStateOf(0.0)
    val currentIV = mutableStateOf(0.0)
    val callPrice = mutableStateOf(0.0)
    val putPrice = mutableStateOf(0.0)
    val straddlePrice = mutableStateOf(0.0)
    val theta = mutableStateOf(0.0)
    val gamma = mutableStateOf(0.0)
    val oiChange = mutableStateOf(0.0)
    val volume = mutableStateOf(0.0)
    val bidAskSpread = mutableStateOf(0.0)
    
    fun saveTradeSignal(tradeSignal: TradeSignal) {
        val json = com.google.gson.Gson().toJson(tradeSignal)
        prefs.edit().putString("trade_signal_${tradeSignal.timestamp}", json).apply()
    }
    
    fun getTradeSignals(): List<TradeSignal> {
        val signals = mutableListOf<TradeSignal>()
        val allKeys = prefs.all.keys
        allKeys.forEach { key ->
            if (key.startsWith("trade_signal_")) {
                val json = prefs.getString(key, "")
                json?.let {
                    val signal = com.google.gson.Gson().fromJson(it, TradeSignal::class.java)
                    signals.add(signal)
                }
            }
        }
        return signals.sortedByDescending { it.timestamp }
    }
    
    fun clearTradeHistory() {
        val allKeys = prefs.all.keys
        allKeys.forEach { key ->
            if (key.startsWith("trade_signal_")) {
                prefs.edit().remove(key).apply()
            }
        }
    }
}

class TradeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        if (modelClass.isAssignableFrom(TradeViewModel::class.java)) {
            return TradeViewModel(context) as VM
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}