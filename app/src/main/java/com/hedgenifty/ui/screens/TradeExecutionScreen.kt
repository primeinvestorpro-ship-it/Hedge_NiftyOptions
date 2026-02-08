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
    var strikePrice by remember { mutableStateOf("22450") }
    var quantity by remember { mutableStateOf("50") }
    var entryPrice by remember { mutableStateOf("120.50") }
    var stopLoss by remember { mutableStateOf("100.00") }
    var target by remember { mutableStateOf("160.00") }
    
    HedgeNiftyOptionsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Order Terminal", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(androidx.compose.material.icons.Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = IndiaNavy,
                        titleContentColor = IndiaWhite,
                        navigationIconContentColor = IndiaWhite
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Trade Type Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TypeButton("CALL", selectedType == TradeType.CALL, IndiaGreen, Modifier.weight(1f)) { selectedType = TradeType.CALL }
                    TypeButton("PUT", selectedType == TradeType.PUT, IndiaSaffron, Modifier.weight(1f)) { selectedType = TradeType.PUT }
                    TypeButton("STRADDLE", selectedType == TradeType.STRADDLE, IndiaNavy, Modifier.weight(1f)) { selectedType = TradeType.STRADDLE }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndiaNavy.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ORDER SPECIFICATIONS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IndiaNavy, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        ThemedInput("Strike Price", strikePrice, { strikePrice = it }, "₹")
                        ThemedInput("Quantity", quantity, { quantity = it }, "QTY")
                        ThemedInput("Entry Price", entryPrice, { entryPrice = it }, "₹")
                        
                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                ThemedInput("Stop Loss", stopLoss, { stopLoss = it }, "SL", IndiaSaffron)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                ThemedInput("Target", target, { target = it }, "TP", IndiaGreen)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = IndiaNavy.copy(alpha = 0.05f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Est. Margin Required", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${(quantity.toDoubleOrNull() ?: 0.0) * (entryPrice.toDoubleOrNull() ?: 0.0)}", fontSize = 20.sp, fontWeight = FontWeight.Black, color = IndiaNavy)
                        }
                        Icon(androidx.compose.material.icons.Icons.Default.Info, contentDescription = null, tint = IndiaNavy.copy(alpha = 0.5f))
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        val ts = TradeSignal(
                            type = selectedType,
                            strikePrice = strikePrice.toDoubleOrNull() ?: 0.0,
                            quantity = quantity.toIntOrNull() ?: 1,
                            entryPrice = entryPrice.toDoubleOrNull() ?: 0.0,
                            stopLoss = stopLoss.toDoubleOrNull() ?: 0.0,
                            target = target.toDoubleOrNull() ?: 0.0,
                            timestamp = System.currentTimeMillis()
                        )
                        onExecuteTrade(ts)
                    },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when(selectedType) {
                            TradeType.CALL -> IndiaGreen
                            TradeType.PUT -> IndiaSaffron
                            TradeType.STRADDLE -> IndiaNavy
                        }
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "PLACE ${selectedType.name} ORDER",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Secured by Indian Fintech Standard",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TypeButton(label: String, selected: Boolean, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        color = if (selected) color else Color.Transparent,
        contentColor = if (selected) IndiaWhite else MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ThemedInput(label: String, value: String, onValueChange: (String) -> Unit, suffix: String, accentColor: Color = IndiaNavy) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor.copy(alpha = 0.7f), modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            suffix = { Text(suffix, fontSize = 12.sp, color = Color.Gray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
            )
        )
    }
}
