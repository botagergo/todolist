package hu.botagergo.todolist.feature_task.mapper

import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task.data.model.Task

fun TaskEntity.toTask(): Task {
    return Task(
        this.title,
        this.comments,
        this.status,
        this.context,
        this.startDate,
        this.startTime,
        this.dueDate,
        this.dueTime,
        this.done,
        this.uid
    )
}

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        this.title,
        this.comments,
        this.status,
        this.context,
        this.startDate,
        this.startTime,
        this.dueDate,
        this.dueTime,
        this.done,
        this.uid
    )
}