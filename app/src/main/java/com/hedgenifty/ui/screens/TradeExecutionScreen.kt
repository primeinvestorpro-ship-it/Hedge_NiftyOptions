package com.hedgenifty.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hedgenifty.models.TradeSignal
import com.hedgenifty.models.TradeType
import com.hedgenifty.ui.theme.HedgeNiftyOptionsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeExecutionScreen(
    onBack: () -> Unit,
    onExecuteTrade: (TradeSignal) -> Unit
) {
    var selectedType by remember { mutableStateOf(TradeType.CALL) }
    var strikePrice by remember { mutableStateOf(0.0) }
    var quantity by remember { mutableStateOf(1) }
    var entryPrice by remember { mutableStateOf(0.0) }
    var stopLoss by remember { mutableStateOf(0.0) }
    var target by remember { mutableStateOf(0.0) }
    
    HedgeNiftyOptionsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Trade Execution",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Trade Type Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Trade Type",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { selectedType = TradeType.CALL },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedType == TradeType.CALL) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text("CALL")
                        }
                        Button(
                            onClick = { selectedType = TradeType.PUT },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedType == TradeType.PUT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text("PUT")
                        }
                        Button(
                            onClick = { selectedType = TradeType.STRADDLE },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedType == TradeType.STRADDLE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text("STRADDLE")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Trade Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Trade Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Strike Price
                    TextField(
                        value = strikePrice.toString(),
                        onValueChange = { strikePrice = it.toDoubleOrNull() ?: 0.0 },
                        label = { Text("Strike Price") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Quantity
                    TextField(
                        value = quantity.toString(),
                        onValueChange = { quantity = it.toIntOrNull() ?: 1 },
                        label = { Text("Quantity") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Entry Price
                    TextField(
                        value = entryPrice.toString(),
                        onValueChange = { entryPrice = it.toDoubleOrNull() ?: 0.0 },
                        label = { Text("Entry Price") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Stop Loss
                    TextField(
                        value = stopLoss.toString(),
                        onValueChange = { stopLoss = it.toDoubleOrNull() ?: 0.0 },
                        label = { Text("Stop Loss") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Target
                    TextField(
                        value = target.toString(),
                        onValueChange = { target = it.toDoubleOrNull() ?: 0.0 },
                        label = { Text("Target") },
                        modifier = Modifier.fillMaxWidth()
                    )
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
                Button(
                    onClick = {
                        val tradeSignal = TradeSignal(
                            type = selectedType,
                            strikePrice = strikePrice,
                            quantity = quantity,
                            entryPrice = entryPrice,
                            stopLoss = stopLoss,
                            target = target,
                            timestamp = System.currentTimeMillis()
                        )
                        onExecuteTrade(tradeSignal)
                    }
                ) {
                    Text("Execute Trade")
                }
            }
        }
    }
}