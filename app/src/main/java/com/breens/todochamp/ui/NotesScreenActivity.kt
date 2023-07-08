package com.breens.todochamp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.breens.todochamp.theme.TodoChampTheme

class NotesScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoChampTheme {
            }
        }
    }
}
