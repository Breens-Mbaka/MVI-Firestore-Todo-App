package com.breens.todochamp.feature_tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.todochamp.common.Result
import com.breens.todochamp.data.model.Task
import com.breens.todochamp.data.repositories.TaskRepository
import com.breens.todochamp.feature_tasks.events.TasksScreenUiEvent
import com.breens.todochamp.feature_tasks.side_effects.TaskScreenSideEffects
import com.breens.todochamp.feature_tasks.state.TasksScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {

    private val _state: MutableStateFlow<TasksScreenUiState> =
        MutableStateFlow(TasksScreenUiState())
    val state: StateFlow<TasksScreenUiState> = _state.asStateFlow()

    private val _effect: Channel<TaskScreenSideEffects> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        sendEvent(TasksScreenUiEvent.GetTasks)
    }

    fun sendEvent(event: TasksScreenUiEvent) {
        reduce(oldState = _state.value, event = event)
    }

    private fun setEffect(builder: () -> TaskScreenSideEffects) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    private fun setState(newState: TasksScreenUiState) {
        _state.value = newState
    }

    private fun reduce(oldState: TasksScreenUiState, event: TasksScreenUiEvent) {
        when (event) {
            is TasksScreenUiEvent.AddTask -> {
                addTask(oldState = oldState, title = event.title, body = event.body)
            }

            is TasksScreenUiEvent.DeleteNote -> {
                deleteNote(oldState = oldState, taskId = event.taskId)
            }

            TasksScreenUiEvent.GetTasks -> {
                getTasks(oldState = oldState)
            }

            is TasksScreenUiEvent.OnChangeAddTaskDialogState -> {
                onChangeAddTaskDialog(oldState = oldState, isShown = event.show)
            }

            is TasksScreenUiEvent.OnChangeUpdateTaskDialogState -> {
                onUpdateAddTaskDialog(oldState = oldState, isShown = event.show)
            }

            is TasksScreenUiEvent.OnChangeTaskBody -> {
                onChangeTaskBody(oldState = oldState, body = event.body)
            }

            is TasksScreenUiEvent.OnChangeTaskTitle -> {
                onChangeTaskTitle(oldState = oldState, title = event.title)
            }

            is TasksScreenUiEvent.SetTaskToBeUpdated -> {
                setTaskToBeUpdated(oldState = oldState, task = event.taskToBeUpdated)
            }

            TasksScreenUiEvent.UpdateNote -> {
                updateNote(oldState = oldState)
            }
        }
    }

    private fun addTask(title: String, body: String, oldState: TasksScreenUiState) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            when (val result = taskRepository.addTask(title = title, body = body)) {
                is Result.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when adding task"
                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Result.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            currentTextFieldTitle = "",
                            currentTextFieldBody = "",
                        ),
                    )

                    sendEvent(TasksScreenUiEvent.OnChangeAddTaskDialogState(show = false))

                    sendEvent(TasksScreenUiEvent.GetTasks)

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = "Task added successfully") }
                }
            }
        }
    }

    private fun getTasks(oldState: TasksScreenUiState) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            when (val result = taskRepository.getAllTasks()) {
                is Result.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your task"
                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Result.Success -> {
                    val tasks = result.data
                    setState(oldState.copy(isLoading = false, tasks = tasks))
                }
            }
        }
    }

    private fun deleteNote(oldState: TasksScreenUiState, taskId: String) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            when (val result = taskRepository.deleteTask(taskId = taskId)) {
                is Result.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when deleting task"
                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Result.Success -> {
                    setState(oldState.copy(isLoading = false))

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = "Task deleted successfully") }

                    sendEvent(TasksScreenUiEvent.GetTasks)
                }
            }
        }
    }

    private fun updateNote(oldState: TasksScreenUiState) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            val title = oldState.currentTextFieldTitle
            val body = oldState.currentTextFieldBody
            val taskToBeUpdated = oldState.taskToBeUpdated

            when (
                val result = taskRepository.updateTask(
                    title = title,
                    body = body,
                    taskId = taskToBeUpdated?.taskId ?: "",
                )
            ) {
                is Result.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when updating task"
                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Result.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            currentTextFieldTitle = "",
                            currentTextFieldBody = "",
                        ),
                    )

                    sendEvent(TasksScreenUiEvent.OnChangeUpdateTaskDialogState(show = false))

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = "Task updated successfully") }

                    sendEvent(TasksScreenUiEvent.GetTasks)
                }
            }
        }
    }

    private fun onChangeAddTaskDialog(oldState: TasksScreenUiState, isShown: Boolean) {
        setState(oldState.copy(isShowAddTaskDialog = isShown))
    }

    private fun onUpdateAddTaskDialog(oldState: TasksScreenUiState, isShown: Boolean) {
        setState(oldState.copy(isShowUpdateTaskDialog = isShown))
    }

    private fun onChangeTaskBody(oldState: TasksScreenUiState, body: String) {
        setState(oldState.copy(currentTextFieldBody = body))
    }

    private fun onChangeTaskTitle(oldState: TasksScreenUiState, title: String) {
        setState(oldState.copy(currentTextFieldTitle = title))
    }

    private fun setTaskToBeUpdated(oldState: TasksScreenUiState, task: Task) {
        setState(oldState.copy(taskToBeUpdated = task))
    }
}
