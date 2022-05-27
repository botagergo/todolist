package hu.botagergo.todolist.feature_task_view.domain

import java.util.*

class TaskViewNotFoundException(val uuid: UUID): Exception("Task View with uuid $uuid not found")