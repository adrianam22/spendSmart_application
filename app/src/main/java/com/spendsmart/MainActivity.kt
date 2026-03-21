package com.spendsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.spendsmart.navigation.NavGraph
import com.spendsmart.ui.theme.SpendSmartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendSmartTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}