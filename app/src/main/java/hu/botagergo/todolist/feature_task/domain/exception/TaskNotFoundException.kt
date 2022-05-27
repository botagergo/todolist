package hu.botagergo.todolist.feature_task.domain.exception

class TaskNotFoundException(val uid: Long): Exception("Task with uid $uid not found")