package hu.botagergo.todolist

import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import java.time.LocalDate
import java.time.LocalTime

object Util {
    
    val task = TaskEntity(
        title="example task", comments="some comment",
        context=Predefined.TaskContextValues.home,
        status=Predefined.TaskStatusValues.nextAction,
        startDate= LocalDate.of(2000, 4, 10),
        startTime= LocalTime.of(14, 55),
        dueDate= LocalDate.of(2000, 5, 11),
        dueTime= LocalTime.of(16, 56),
        done=true
    )

}