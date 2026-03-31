package com.example.student_planner_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.student_planner_app.model.Task

@Composable
fun StatsScreen(navController: NavController, email: String) {
    val db = remember { FirebaseFirestore.getInstance() }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var selectedTab by remember { mutableStateOf(3) }

    LaunchedEffect(email) {
        db.collection("tasks")
            .whereEqualTo("userEmail", email)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    tasks = snapshot.documents.mapNotNull { doc ->
                        Task(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            deadline = doc.getString("deadline") ?: "",
                            isCompleted = doc.getBoolean("completed") ?: false,
                            category = doc.getString("category") ?: "Study",
                            userEmail = doc.getString("userEmail") ?: ""
                        )
                    }
                }
            }
    }

    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val pendingTasks = totalTasks - completedTasks
    val studyTasks = tasks.count { it.category == "Study" }
    val assignmentTasks = tasks.count { it.category == "Assignment" }
    val examTasks = tasks.count { it.category == "Exam" }
    val progress = if (totalTasks == 0) 0f else completedTasks.toFloat() / totalTasks

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 12.dp,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController.navigate("dashboard/$email") {
                            popUpTo("dashboard/$email") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF5C35CC),
                        selectedTextColor = Color(0xFF5C35CC),
                        indicatorColor = Color(0xFFEDE7F6),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate("addtask/$email")
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Task") },
                    label = { Text("Add Task", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF5C35CC),
                        selectedTextColor = Color(0xFF5C35CC),
                        indicatorColor = Color(0xFFEDE7F6),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Text("📊", fontSize = 20.sp) },
                    label = { Text("Stats", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF5C35CC),
                        selectedTextColor = Color(0xFF5C35CC),
                        indicatorColor = Color(0xFFEDE7F6),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navController.navigate("profile/$email")
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF5C35CC),
                        selectedTextColor = Color(0xFF5C35CC),
                        indicatorColor = Color(0xFFEDE7F6),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F0FF))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF4527A0), Color(0xFF7B52E0))
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        "📊 Statistics",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Your task progress overview",
                        color = Color(0xFFD1C4E9),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Overall Progress Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Overall Progress",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF5C35CC)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCircle(value = totalTasks.toString(), label = "Total", color = Color(0xFF5C35CC))
                        StatCircle(value = completedTasks.toString(), label = "Done", color = Color(0xFF2E7D32))
                        StatCircle(value = pendingTasks.toString(), label = "Pending", color = Color(0xFFC62828))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Completion Rate", fontSize = 13.sp, color = Color.Gray)
                        Text(
                            "${(progress * 100).toInt()}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5C35CC)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFF5C35CC),
                        trackColor = Color(0xFFEDE7F6)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Breakdown
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Tasks by Category",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF5C35CC)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CategoryBar(
                        label = "📖 Study",
                        count = studyTasks,
                        total = totalTasks,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CategoryBar(
                        label = "📝 Assignment",
                        count = assignmentTasks,
                        total = totalTasks,
                        color = Color(0xFF1565C0)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CategoryBar(
                        label = "📚 Exam",
                        count = examTasks,
                        total = totalTasks,
                        color = Color(0xFFC62828)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Completed vs Pending Chart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Completed vs Pending",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF5C35CC)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Bar Chart
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        BarChart(
                            value = completedTasks,
                            maxValue = if (totalTasks == 0) 1 else totalTasks,
                            label = "Completed",
                            color = Color(0xFF5C35CC)
                        )
                        BarChart(
                            value = pendingTasks,
                            maxValue = if (totalTasks == 0) 1 else totalTasks,
                            label = "Pending",
                            color = Color(0xFFFFCC80)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatCircle(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(50))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun CategoryBar(label: String, count: Int, total: Int, color: Color) {
    val progress = if (total == 0) 0f else count.toFloat() / total
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, modifier = Modifier.width(110.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("$count", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun BarChart(value: Int, maxValue: Int, label: String, color: Color) {
    val heightFraction = if (maxValue == 0) 0f else value.toFloat() / maxValue
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(150.dp)
    ) {
        Text(
            "$value",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(60.dp)
                .height((100 * heightFraction).dp.coerceAtLeast(8.dp))
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}