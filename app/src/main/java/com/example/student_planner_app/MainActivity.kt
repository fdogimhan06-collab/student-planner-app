package com.example.student_planner_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.example.student_planner_app.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashScreen(navController)
                        }
                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("register") {
                            RegisterScreen(navController)
                        }
                        composable("dashboard/{email}") { back ->
                            val email = back.arguments?.getString("email") ?: ""
                            DashboardScreen(navController, email)
                        }
                        composable("addtask/{email}") { back ->
                            val email = back.arguments?.getString("email") ?: ""
                            AddTaskScreen(navController, email)
                        }
                        composable("stats/{email}") { back ->
                            val email = back.arguments?.getString("email") ?: ""
                            StatsScreen(navController, email)
                        }
                        composable("profile/{email}") { back ->
                            val email = back.arguments?.getString("email") ?: ""
                            ProfileScreen(navController, email)
                        }
                        composable("edittask/{email}/{taskId}") { back ->
                            val email = back.arguments?.getString("email") ?: ""
                            val taskId = back.arguments?.getString("taskId") ?: ""
                            EditTaskScreen(navController, email, taskId)
                        }
                    }
                }
            }
        }
    }
}