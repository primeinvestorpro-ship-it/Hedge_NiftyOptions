package com.hedgenifty.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hedgenifty.models.TradeRecord
import com.hedgenifty.models.TradeStatus
import com.hedgenifty.ui.theme.HedgeNiftyOptionsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeHistoryScreen(
    onBack: () -> Unit,
    onRefresh: () -> Unit
) {
    var tradeHistory by remember { mutableStateOf(listOf(
        TradeRecord("T1", TradeSignal(TradeType.CALL, 22400.0, 50, 110.0, 90.0, 150.0, System.currentTimeMillis() - 86400000), TradeStatus.CLOSED, 2000.0),
        TradeRecord("T2", TradeSignal(TradeType.PUT, 22500.0, 50, 95.0, 115.0, 60.0, System.currentTimeMillis() - 43200000), TradeStatus.EXECUTED, -750.0),
        TradeRecord("T3", TradeSignal(TradeType.STRADDLE, 22450.0, 100, 235.0, 200.0, 300.0, System.currentTimeMillis()), TradeStatus.PENDING, 0.0)
    )) }
    
    HedgeNiftyOptionsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Trade Ledger", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(androidx.compose.material.icons.Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = onRefresh) {
                            Icon(androidx.compose.material.icons.Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = IndiaNavy,
                        titleContentColor = IndiaWhite,
                        navigationIconContentColor = IndiaWhite,
                        actionIconContentColor = IndiaWhite
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (tradeHistory.isNotEmpty()) {
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(tradeHistory.size) { index ->
                            TradeHistoryItem(tradeHistory[index])
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(androidx.compose.material.icons.Icons.Default.History, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                            Text("No trades recorded yet", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TradeHistoryItem(trade: TradeRecord) {
    val isProfit = trade.profitLoss > 0
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = when(trade.tradeSignal.type) {
                            TradeType.CALL -> IndiaGreen
                            TradeType.PUT -> IndiaSaffron
                            else -> IndiaNavy
                        },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            trade.tradeSignal.type.name,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = IndiaWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(trade.tradeSignal.strikePrice.toInt().toString(), fontWeight = FontWeight.Black, fontSize = 16.sp, color = IndiaNavy)
                }
                Text("Order ID: ${trade.id}", fontSize = 10.sp, color = Color.Gray)
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (trade.profitLoss == 0.0) trade.status.name else (if (isProfit) "+₹${trade.profitLoss}" else "-₹${-trade.profitLoss}"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = if (trade.profitLoss == 0.0) IndiaNavy else (if (isProfit) IndiaGreen else IndiaSaffron)
                )
                Text(
                    text = if (trade.profitLoss == 0.0) "PENDING" else (if (isProfit) "PROFIT" else "LOSS"),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (trade.profitLoss == 0.0) Color.Gray else (if (isProfit) IndiaGreen else IndiaSaffron)
                )
            }
        }
    }
}
