package com.example.student_planner_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EditTaskScreen(navController: NavController, email: String, taskId: String) {
    val db = remember { FirebaseFirestore.getInstance() }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Study") }
    var errorMsg by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(1) }
    val categories = listOf("Study", "Assignment", "Exam")

    // Load task data from Firestore
    LaunchedEffect(taskId) {
        db.collection("tasks").document(taskId).get()
            .addOnSuccessListener { doc ->
                title = doc.getString("title") ?: ""
                description = doc.getString("description") ?: ""
                deadline = doc.getString("deadline") ?: ""
                selectedCategory = doc.getString("category") ?: "Study"
            }
    }

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
                    onClick = { selectedTab = 1 },
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
        ) {
            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF5C35CC))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        "Edit Task",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF5C35CC),
                        focusedLabelColor = Color(0xFF5C35CC)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF5C35CC),
                        focusedLabelColor = Color(0xFF5C35CC)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Deadline (e.g. 2024-12-31)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF5C35CC),
                        focusedLabelColor = Color(0xFF5C35CC)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Category",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5C35CC)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF5C35CC),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (title.isBlank() || deadline.isBlank()) {
                            errorMsg = "Enter a title and deadline."
                            return@Button
                        }
                        isLoading = true
                        db.collection("tasks").document(taskId)
                            .update(
                                mapOf(
                                    "title" to title,
                                    "description" to description,
                                    "deadline" to deadline,
                                    "category" to selectedCategory
                                )
                            )
                            .addOnSuccessListener {
                                isLoading = false
                                navController.navigate("dashboard/$email") {
                                    popUpTo("dashboard/$email") { inclusive = true }
                                }
                            }
                            .addOnFailureListener {
                                isLoading = false
                                errorMsg = "Unable to update task."
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5C35CC)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            "Update Task",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
