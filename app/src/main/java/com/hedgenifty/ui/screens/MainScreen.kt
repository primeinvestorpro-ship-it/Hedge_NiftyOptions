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
        spotPrice = 22453.30,
        expiryDate = "25-APR-2026",
        currentIV = 14.5,
        atmStrike = 22450.0,
        callPrice = 120.5,
        putPrice = 115.2,
        straddlePrice = 235.7,
        theta = -12.4,
        gamma = 0.002,
        oiChange = 1.5,
        volume = 500000.0,
        bidAskSpread = 0.1,
        timestamp = System.currentTimeMillis()
    )) }
    
    HedgeNiftyOptionsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Hedge Nifty Options",
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
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
                // Make in India Branding
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier.size(24.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = IndiaSaffron
                        ) {}
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            modifier = Modifier.size(24.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = IndiaWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, IndiaNavy)
                        ) {
                            // Simple Dot for Ashoka Chakra
                            Box(contentAlignment = Alignment.Center) {
                                Surface(modifier = Modifier.size(4.dp), color = IndiaNavy, shape = androidx.compose.foundation.shape.CircleShape) {}
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            modifier = Modifier.size(24.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = IndiaGreen
                        ) {}
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "MAKE IN INDIA",
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Live Data Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Live NIFTY 50",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = IndiaNavy
                            )
                            Surface(
                                color = IndiaGreen.copy(alpha = 0.1f),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "LIVE",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = IndiaGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = IndiaNavy.copy(alpha = 0.1f))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            InfoItem("Spot Price", "₹${niftyData.spotPrice}", IndiaNavy)
                            InfoItem("ATM Strike", "${niftyData.atmStrike}", IndiaNavy)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            InfoItem("Expiry", niftyData.expiryDate, IndiaSaffron)
                            InfoItem("IV", "${niftyData.currentIV}%", IndiaGreen)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Dashboard Grid
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DashboardButton(
                            text = "Strategy\nCalculator",
                            icon = androidx.compose.material.icons.Icons.Default.Calculate,
                            modifier = Modifier.weight(1f),
                            containerColor = IndiaSaffron,
                            onClick = onCalculatorClick
                        )
                        DashboardButton(
                            text = "Trade\nExecution",
                            icon = androidx.compose.material.icons.Icons.Default.TrendingUp,
                            modifier = Modifier.weight(1f),
                            containerColor = IndiaGreen,
                            onClick = onTradeClick
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DashboardButton(
                            text = "Trade\nHistory",
                            icon = androidx.compose.material.icons.Icons.Default.History,
                            modifier = Modifier.weight(1f),
                            containerColor = IndiaNavy,
                            onClick = onHistoryClick
                        )
                        DashboardButton(
                            text = "Live\nInsights",
                            icon = androidx.compose.material.icons.Icons.Default.BarChart,
                            modifier = Modifier.weight(1f),
                            containerColor = IndiaSaffron,
                            onClick = onLiveDataClick
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = onRefresh,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(androidx.compose.material.icons.Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("REFRESH DATA", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, color: Color) {
    Column {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
    }
}

@Composable
fun DashboardButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(120.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = IndiaWhite,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text,
                color = IndiaWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        }
    }
}

