package com.hedgenifty.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hedgenifty.data.NiftyData
import com.hedgenifty.data.OptionChainData
import com.hedgenifty.network.NetworkManager
import com.hedgenifty.ui.theme.HedgeNiftyOptionsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveDataScreen(
    niftyData: NiftyData?,
    optionsChain: List<OptionChainData>?,
    onRefresh: () -> Unit,
    onBack: () -> Unit
) {
    val networkManager = remember { NetworkManager() }
    var liveNifty by remember { mutableStateOf(niftyData) }
    var liveOptions by remember { mutableStateOf(optionsChain) }

    LaunchedEffect(Unit) {
        val nd = networkManager.getLiveNiftyData()
        val oc = networkManager.getOptionsChain()
        if (nd != null) liveNifty = nd
        if (oc != null) liveOptions = oc
    }
    
    HedgeNiftyOptionsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Market Data",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Row {
                    Button(onClick = onRefresh) {
                        Text("Refresh")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // NIFTY Live Data Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "NIFTY 50 Live",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (liveNifty != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Spot Price", fontSize = 12.sp)
                                Text(
                                    liveNifty!!.spotPrice.toString(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text("IV", fontSize = 12.sp)
                                Text(
                                    "${liveNifty!!.currentIV}%",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("ATM Strike", fontSize = 12.sp)
                                Text(liveNifty!!.atmStrike.toString(), fontSize = 16.sp)
                            }
                            Column {
                                Text("Call", fontSize = 12.sp)
                                Text(liveNifty!!.callPrice.toString(), fontSize = 16.sp)
                            }
                            Column {
                                Text("Put", fontSize = 12.sp)
                                Text(liveNifty!!.putPrice.toString(), fontSize = 16.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Theta", fontSize = 12.sp)
                                Text(liveNifty!!.theta.toString(), fontSize = 16.sp)
                            }
                            Column {
                                Text("Gamma", fontSize = 12.sp)
                                Text(liveNifty!!.gamma.toString(), fontSize = 16.sp)
                            }
                            Column {
                                Text("OI Change", fontSize = 12.sp)
                                Text(liveNifty!!.oiChange.toString(), fontSize = 16.sp)
                            }
                        }
                    } else {
                        Text("Loading data...", fontSize = 16.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Options Chain Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Options Chain",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (liveOptions != null && liveOptions!!.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.height(400.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(liveOptions!!.take(20)) { option ->
                                OptionsChainItem(option)
                            }
                        }
                    } else {
                        Text("Loading options chain...", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun OptionsChainItem(option: OptionChainData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Strike: ${option.strikePrice}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("C-IV: ${option.callIV}%", fontSize = 12.sp)
                Text("C-OI: ${option.callOI}", fontSize = 12.sp)
            }
            Column {
                Text("P-IV: ${option.putIV}%", fontSize = 12.sp)
                Text("P-OI: ${option.putOI}", fontSize = 12.sp)
                Text("P-Vol: ${option.putVolume}", fontSize = 12.sp)
            }
        }
    }
}