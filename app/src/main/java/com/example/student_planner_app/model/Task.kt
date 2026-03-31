package com.example.student_planner_app.model

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val deadline: String = "",
    val isCompleted: Boolean = false,
    val category: String = "Study",
    val userEmail: String = ""
)