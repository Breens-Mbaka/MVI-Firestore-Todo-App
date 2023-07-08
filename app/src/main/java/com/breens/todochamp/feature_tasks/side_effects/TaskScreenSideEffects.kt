package com.breens.todochamp.feature_tasks.side_effects

sealed class TaskScreenSideEffects {
    data class ShowSnackBarMessage(val message: String) : TaskScreenSideEffects()
}
