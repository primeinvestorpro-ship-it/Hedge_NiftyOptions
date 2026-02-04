package com.hedgenifty.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hedgenifty.data.StrategyResult
import com.hedgenifty.data.EdgeScores
import com.hedgenifty.data.ProbabilityExpectancy
import com.hedgenifty.data.RegimeProbs
import com.hedgenifty.data.TradeDecision
import com.hedgenifty.data.RiskMetrics
import com.hedgenifty.data.Filters
import com.hedgenifty.ui.theme.HedgeNiftyOptionsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrategyCalculatorScreen(
    onBack: () -> Unit,
    onCalculate: () -> Unit
) {
    var strategyResult by remember { mutableStateOf(StrategyResult(
        edgeScores = EdgeScores(0, 0, 0),
        probabilityExpectancy = ProbabilityExpectancy(0.0, 0.0, 0, 0.0, 0.0),
        regimeProbs = RegimeProbs(0.0, 0.0, 0.0, 0.0, emptyMap()),
        tradeDecision = TradeDecision("", 0.0, 0, 0, 0.0, 0.0, 0.0),
        riskMetrics = RiskMetrics(0.0, 0.0, 0.0, ""),
        filters = Filters(false, false, false, false, false, false),
        timestamp = 0
    )) }
    
    LaunchedEffect(Unit) {
        // Calculate strategy when needed
    }
    
    HedgeNiftyOptionsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Strategy Calculator",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Strategy Results Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Strategy Analysis",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Edge Scores
                    Text(
                        text = "Edge Scores:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Call: ${strategyResult.edgeScores.callEdge}")
                    Text("Put: ${strategyResult.edgeScores.putEdge}")
                    Text("Straddle: ${strategyResult.edgeScores.straddleEdge}")
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Trade Decision
                    Text(
                        text = "Trade Decision: ${strategyResult.tradeDecision.decision}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = when(strategyResult.tradeDecision.decision) {
                            "BUY" -> MaterialTheme.colorScheme.primary
                            "NO TRADE" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Probability and Expectancy
                    Text(
                        text = "Win Probability: ${strategyResult.probabilityExpectancy.winProbability}%",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Net Expectancy: ${strategyResult.probabilityExpectancy.netExpectancy}",
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
                Button(onClick = onBack) {
                    Text("Back")
                }
                Button(onClick = onCalculate) {
                    Text("Calculate")
                }
            }
        }
    }
}