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
    var tradeHistory by remember { mutableStateOf(emptyList<TradeRecord>()) }
    
    LaunchedEffect(Unit) {
        // Fetch trade history
    }
    
    HedgeNiftyOptionsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Trade History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Trade History Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Recent Trades",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (tradeHistory.isNotEmpty()) {
                        tradeHistory.forEach { trade ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Type: ${trade.tradeSignal.type}",
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Strike: ${trade.tradeSignal.strikePrice}",
                                        fontSize = 14.sp
                                    )
                                     Text(
                                        text = "Status: ${trade.status}",
                                        fontSize = 14.sp,
                                        color = when(trade.status) {
                                            TradeStatus.EXECUTED -> MaterialTheme.colorScheme.primary
                                            TradeStatus.CLOSED -> MaterialTheme.colorScheme.secondary
                                            TradeStatus.FAILED -> MaterialTheme.colorScheme.error
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                    Text(
                                        text = "P/L: ${trade.profitLoss}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else {
                        Text(
                            text = "No trade history available",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onBack) {
                    Text("Back")
                }
                Button(onClick = onRefresh) {
                    Text("Refresh")
                }
            }
        }
    }
}