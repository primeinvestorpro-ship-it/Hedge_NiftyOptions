package com.hedgenifty

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hedgenifty.ui.theme.HedgeNiftyOptionsTheme
import com.hedgenifty.ui.screens.MainScreen
import com.hedgenifty.ui.screens.StrategyCalculatorScreen
import com.hedgenifty.ui.screens.TradeExecutionScreen
import com.hedgenifty.ui.screens.TradeHistoryScreen
import com.hedgenifty.ui.screens.LiveDataScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HedgeNiftyOptionsTheme {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main",
        route = "app"
    ) {
        composable("main") {
            MainScreen(
                onCalculatorClick = { navController.navigate("calculator") },
                onTradeClick = { navController.navigate("trade") },
                onHistoryClick = { navController.navigate("history") },
                onRefresh = { /* Refresh data */ },
                onLiveDataClick = { navController.navigate("live") }
            )
        }
        composable("calculator") {
            StrategyCalculatorScreen(
                onBack = { navController.navigateUp() },
                onCalculate = { /* Calculate strategy */ }
            )
        }
        composable("trade") {
            TradeExecutionScreen(
                onBack = { navController.navigateUp() },
                onExecuteTrade = { /* Execute trade */ }
            )
        }
        composable("history") {
            TradeHistoryScreen(
                onBack = { navController.navigateUp() },
                onRefresh = { /* Refresh history */ }
            )
        }
        composable("live") {
            LiveDataScreen(
                niftyData = null,
                optionsChain = null,
                onRefresh = { /* refresh live data */ },
                onBack = { navController.popBackStack() }
            )
        }
    }
}