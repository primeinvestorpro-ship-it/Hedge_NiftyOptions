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
        // Mocking some options data if missing
        if (oc != null && oc.isNotEmpty()) {
            liveOptions = oc
        } else {
            val mockOptions = mutableListOf<OptionChainData>()
            for (i in -5..5) {
                val strike = 22450.0 + (i * 50)
                mockOptions.add(OptionChainData(
                    strikePrice = strike,
                    callOI = 1250000.0 + (i * 10000),
                    putOI = 1100000.0 - (i * 10000),
                    callVolume = 50000.0,
                    putVolume = 45000.0,
                    callIV = 14.2,
                    putIV = 15.1,
                    callBid = 120.5,
                    callAsk = 121.0,
                    putBid = 110.2,
                    putAsk = 110.8,
                    timestamp = System.currentTimeMillis()
                ))
            }
            liveOptions = mockOptions
        }
    }
    
    HedgeNiftyOptionsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Market Insights", fontWeight = FontWeight.Bold) },
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
                    .padding(16.dp)
            ) {
                // NIFTY Live Data Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndiaNavy.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(modifier = Modifier.size(8.dp), shape = androidx.compose.foundation.shape.CircleShape, color = IndiaGreen) {}
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("NIFTY 50 INDEX", fontWeight = FontWeight.Black, fontSize = 12.sp, color = IndiaNavy, letterSpacing = 1.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        if (liveNifty != null) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("Spot Price", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("₹${liveNifty!!.spotPrice}", fontSize = 28.sp, fontWeight = FontWeight.Black, color = IndiaNavy)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("IV", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("${liveNifty!!.currentIV}%", fontSize = 28.sp, fontWeight = FontWeight.Black, color = IndiaGreen)
                                }
                            }
                            
                            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
                            
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                MiniMetric("ATM", "${liveNifty!!.atmStrike}")
                                MiniMetric("CALL", "₹${liveNifty!!.callPrice}")
                                MiniMetric("PUT", "₹${liveNifty!!.putPrice}")
                            }
                        } else {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Options Chain Header
                Text(
                    text = "OPTIONS CHAIN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // Table Header
                Row(
                    modifier = Modifier.fillMaxWidth().background(IndiaNavy).padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("CALL OI", color = IndiaWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("STRIKE", color = IndiaWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Text("PUT OI", color = IndiaWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                }
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    if (liveOptions != null) {
                        items(liveOptions!!) { option ->
                            OptionsChainRow(option)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MiniMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun OptionsChainRow(option: OptionChainData) {
    val isATM = option.strikePrice == 22450.0 // Simplified ATM check
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isATM) IndiaSaffron.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface)
            .padding(vertical = 12.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Call OI
        Text(
            "${(option.callOI/1000000.0).format(1)}M",
            fontSize = 13.sp,
            color = if (isATM) IndiaSaffron else IndiaGreen,
            fontWeight = if (isATM) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        
        // Strike Center
        Surface(
            color = if (isATM) IndiaSaffron else Color.Transparent,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
        ) {
            Text(
                option.strikePrice.toInt().toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = if (isATM) IndiaWhite else IndiaNavy,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
        
        // Put OI
        Text(
            "${(option.putOI/1000000.0).format(1)}M",
            fontSize = 13.sp,
            color = if (isATM) IndiaSaffron else IndiaGreen,
            fontWeight = if (isATM) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
