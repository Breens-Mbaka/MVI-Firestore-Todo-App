package com.breens.todochamp.feature_tasks.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breens.todochamp.data.model.Task

@Composable
fun TaskCardComponent(
    deleteTask: (String) -> Unit,
    updateTask: (Task) -> Unit,
    task: Task,
) {

    Log.d("TASK: ", "$task")
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = task.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = task.body,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = task.createdAt,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Start,
                )
            }

            Column(verticalArrangement = Arrangement.SpaceAround) {
                IconButton(onClick = {
                    deleteTask(task.taskId)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                    )
                }

                IconButton(onClick = {
                    updateTask(task)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "Update",
                    )
                }
            }
        }
    }
}
