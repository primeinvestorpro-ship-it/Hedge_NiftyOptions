package com.hedgenifty

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TradeNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val tradeType = intent.getStringExtra("TRADE_TYPE") ?: "UNKNOWN"
        val strikePrice = intent.getDoubleExtra("STRIKE_PRICE", 0.0)
        val entryPrice = intent.getDoubleExtra("ENTRY_PRICE", 0.0)
        
        val notification = NotificationCompat.Builder(context, "trade_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Trade Signal: $tradeType")
            .setContentText("Strike: $strikePrice | Entry: $entryPrice")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
            
        NotificationManagerCompat.from(context).notify(1, notification)
    }
}