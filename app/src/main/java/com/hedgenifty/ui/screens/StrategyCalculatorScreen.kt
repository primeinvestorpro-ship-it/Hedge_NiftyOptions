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
        edgeScores = EdgeScores(75, 42, 68),
        probabilityExpectancy = ProbabilityExpectancy(68.5, 45.0, 60, 55.0, 1.25),
        regimeProbs = RegimeProbs(0.1, 0.4, 0.3, 0.2, emptyMap()),
        tradeDecision = TradeDecision("BUY CALL", 22450.0, 15, 60, 2.5, 22400.0, 22550.0),
        riskMetrics = RiskMetrics(-1.2, 0.65, 0.15, "Low Volume"),
        filters = Filters(true, true, true, true, false, true),
        timestamp = System.currentTimeMillis()
    )) }
    
    HedgeNiftyOptionsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Strategy Calculator", fontWeight = FontWeight.Bold) },
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
                // Analysis Result Header
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Recommended Action", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                        Text(
                            text = strategyResult.tradeDecision.decision,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = if (strategyResult.tradeDecision.decision.contains("BUY")) IndiaGreen else IndiaSaffron
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(androidx.compose.material.icons.Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(16.dp), tint = IndiaNavy)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Entry Window: ${strategyResult.tradeDecision.entryWindow} mins", fontSize = 12.sp, color = IndiaNavy)
                        }
                    }
                }

                // Grid of Scores
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ScoreCard("Call Edge", "${strategyResult.edgeScores.callEdge}", IndiaSaffron, Modifier.weight(1f))
                    ScoreCard("Put Edge", "${strategyResult.edgeScores.putEdge}", IndiaGreen, Modifier.weight(1f))
                    ScoreCard("Straddle", "${strategyResult.edgeScores.straddleEdge}", IndiaNavy, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detailed Metrics
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        MetricRow("Win Probability", "${strategyResult.probabilityExpectancy.winProbability}%", true)
                        MetricRow("Expected Move", "${strategyResult.probabilityExpectancy.expectedMove} pts", false)
                        MetricRow("Net Expectancy", "${strategyResult.probabilityExpectancy.netExpectancy}", true)
                        MetricRow("Strike Price", "${strategyResult.tradeDecision.strike}", false)
                        MetricRow("Stop Loss", "₹${strategyResult.tradeDecision.stopLoss}", true)
                        MetricRow("Target", "₹${strategyResult.tradeDecision.profitTarget}", false)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Filters status
                Text(
                    text = "STRATEGY FILTERS",
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                FilterTag("IV Velocity Filter", strategyResult.filters.ivVelocityFilter)
                FilterTag("Straddle Math Filter", strategyResult.filters.straddleMathFilter)
                FilterTag("OI Micro Filter", strategyResult.filters.oiMicroFilter)
                FilterTag("Gamma Amplification Filter", strategyResult.filters.gammaAmplificationFilter)
                FilterTag("Microstructure Filter", strategyResult.filters.microstructureFilter)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onCalculate,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndiaNavy)
                ) {
                    Text("RE-CALCULATE STRATEGY", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ScoreCard(label: String, score: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
            Text(score, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}

@Composable
fun MetricRow(label: String, value: String, isAlternate: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isAlternate) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun FilterTag(label: String, passed: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (passed) androidx.compose.material.icons.Icons.Default.CheckCircle else androidx.compose.material.icons.Icons.Default.Cancel,
            contentDescription = null,
            tint = if (passed) IndiaGreen else Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (passed) MaterialTheme.colorScheme.onSurface else Color.Gray,
            fontWeight = if (passed) FontWeight.Medium else FontWeight.Normal
        )
    }
}
