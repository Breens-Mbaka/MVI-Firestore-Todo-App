package com.breens.todochamp.feature_tasks.events

import com.breens.todochamp.data.model.Task

sealed class TasksScreenUiEvent {
    object GetTasks : TasksScreenUiEvent()

    data class AddTask(val title: String, val body: String) : TasksScreenUiEvent()

    object UpdateNote : TasksScreenUiEvent()

    data class DeleteNote(val taskId: String) : TasksScreenUiEvent()

    data class OnChangeTaskTitle(val title: String) : TasksScreenUiEvent()

    data class OnChangeTaskBody(val body: String) : TasksScreenUiEvent()

    data class OnChangeAddTaskDialogState(val show: Boolean) : TasksScreenUiEvent()

    data class OnChangeUpdateTaskDialogState(val show: Boolean) :
        TasksScreenUiEvent()

    data class SetTaskToBeUpdated(val taskToBeUpdated: Task) : TasksScreenUiEvent()
}
