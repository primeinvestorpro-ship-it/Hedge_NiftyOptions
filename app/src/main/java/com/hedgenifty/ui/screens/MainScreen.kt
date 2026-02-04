package com.hedgenifty.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hedgenifty.data.NiftyData
import com.hedgenifty.ui.theme.HedgeNiftyOptionsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCalculatorClick: () -> Unit,
    onTradeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onRefresh: () -> Unit,
    onLiveDataClick: () -> Unit
) {
    var niftyData by remember { mutableStateOf(NiftyData(
        spotPrice = 0.0,
        expiryDate = "",
        currentIV = 0.0,
        atmStrike = 0.0,
        callPrice = 0.0,
        putPrice = 0.0,
        straddlePrice = 0.0,
        theta = 0.0,
        gamma = 0.0,
        oiChange = 0.0,
        volume = 0.0,
        bidAskSpread = 0.0,
        timestamp = 0
    )) }
    
    LaunchedEffect(Unit) {
        // Fetch live data
    }
    
    HedgeNiftyOptionsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hedge Nifty Options",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Live Data Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Live NIFTY Data",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Spot Price: ${niftyData.spotPrice}",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Expiry: ${niftyData.expiryDate}",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "ATM Strike: ${niftyData.atmStrike}",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "IV: ${niftyData.currentIV}%",
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onCalculatorClick) {
                    Text("Strategy Calculator")
                }
                Button(onClick = onTradeClick) {
                    Text("Trade Execution")
                }
                Button(onClick = onHistoryClick) {
                    Text("Trade History")
                }
                Button(onClick = onLiveDataClick) {
                    Text("Live Data")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(onClick = onRefresh) {
                Text("Refresh Data")
            }
        }
    }
}
