package hu.botagergo.todolist.exception

class TaskNotFoundException(val taskUid: Long): Exception("Task with uid $taskUid not found") {}