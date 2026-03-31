package com.example.student_planner_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
fun DashboardScreen(navController: NavController, email: String, isDarkMode: Boolean = false) {
    val db = remember { FirebaseFirestore.getInstance() }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var userName by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(email) {
        db.collection("users").document(email).get()
            .addOnSuccessListener { doc ->
                userName = doc.getString("name") ?: "Student"
            }
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


    val filteredTasks = if (searchQuery.isBlank()) tasks
    else tasks.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
    }
    val completedCount = tasks.count { it.isCompleted }
    val pendingCount = tasks.size - completedCount
    val progress = if (tasks.isEmpty()) 0f else completedCount.toFloat() / tasks.size

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 12.dp,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
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
                    onClick = {
                        selectedTab = 3
                        navController.navigate("stats/$email")
                    },
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
                        navController.navigate("profile/$email/$isDarkMode")
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
                    .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 28.dp)
            ) {
                Column {
                    Text(
                        "👋 Hello,",
                        color = Color(0xFFD1C4E9),
                        fontSize = 16.sp
                    )
                    Text(
                        userName.ifEmpty { "Student" },
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Total
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.15f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "${tasks.size}",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Total", color = Color(0xFFD1C4E9), fontSize = 12.sp)
                            }
                        }
                        // Done
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.15f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "$completedCount",
                                    color = Color(0xFF80CBC4),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Done", color = Color(0xFFD1C4E9), fontSize = 12.sp)
                            }
                        }
                        // Pending
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.15f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "$pendingCount",
                                    color = Color(0xFFFFCC80),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Pending", color = Color(0xFFD1C4E9), fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Progress", color = Color(0xFFD1C4E9), fontSize = 13.sp)
                        Text(
                            "${(progress * 100).toInt()}%",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFF80CBC4),
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Today's Tasks",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4527A0)
                )
                Text(
                    "${tasks.size} tasks",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("🔍 Search tasks...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5C35CC),
                    focusedLabelColor = Color(0xFF5C35CC)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📋", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No tasks yet!",
                            color = Color(0xFF4527A0),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Tap Add Task to get started",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredTasks) { task ->
                        TaskCard(
                            task = task,
                            onComplete = {
                                db.collection("tasks").document(task.id)
                                    .update("completed", !task.isCompleted)
                            },
                            onDelete = {
                                db.collection("tasks").document(task.id).delete()
                            },
                            onEdit = {
                                navController.navigate("edittask/$email/${task.id}")
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onComplete: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    val categoryColor = when (task.category) {
        "Assignment" -> Color(0xFF1565C0)
        "Exam" -> Color(0xFFC62828)
        else -> Color(0xFF2E7D32)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) Color(0xFFE8F5E9) else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onComplete() },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF5C35CC))
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (task.isCompleted) Color.Gray else Color(0xFF4527A0)
                    )
                    if (task.description.isNotBlank()) {
                        Text(task.description, fontSize = 13.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⏰ ${task.deadline}", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = categoryColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                task.category,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 11.sp,
                                color = categoryColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFEF5350)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(
                onClick = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF5C35CC)
                )
            ) {
                Text("✏️ Edit Task", fontSize = 13.sp)
            }
        }
    }
}