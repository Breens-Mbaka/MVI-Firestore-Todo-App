package com.breens.todochamp.data.repositories

import com.breens.todochamp.common.Result
import com.breens.todochamp.data.model.Task

interface TaskRepository {
    suspend fun addTask(title: String, body: String): Result<Unit>

    suspend fun getAllTasks(): Result<List<Task>>

    suspend fun deleteTask(taskId: String): Result<Unit>

    suspend fun updateTask(title: String, body: String, taskId: String): Result<Unit>
}
