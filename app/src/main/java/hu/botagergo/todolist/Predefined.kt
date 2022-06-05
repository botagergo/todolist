package hu.botagergo.todolist

import hu.botagergo.todolist.core.util.*
import hu.botagergo.todolist.feature_task_view.domain.model.filter.*
import hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate.Equals
import hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate.LessEqual
import hu.botagergo.todolist.feature_task_view.data.group.DueGrouper
import hu.botagergo.todolist.feature_task_view.data.group.Grouper
import hu.botagergo.todolist.feature_task_view.data.group.PropertyGrouper
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task.data.model.TaskEnumPropertyValueEntity
import hu.botagergo.todolist.feature_task.data.model.PropertyEntity
import hu.botagergo.todolist.feature_task.domain.repository.TaskEnumPropertyValueRepository
import hu.botagergo.todolist.feature_task.domain.repository.TaskPropertyRepository
import hu.botagergo.todolist.feature_task_view.data.sorter.ManualTaskSorter
import hu.botagergo.todolist.feature_task_view.domain.TaskFilterRepository
import java.time.LocalDate
import javax.inject.Inject

class Predefined {

    @Inject lateinit var taskPropertyRepository: TaskPropertyRepository
    @Inject lateinit var taskEnumPropertyValueRepository: TaskEnumPropertyValueRepository
    @Inject lateinit var taskFilterRepository: TaskFilterRepository

    object TaskProperty {
        val title = PropertyEntity(
            "title", "title",
            PropertyEntity.TaskPropertyType.STRING
        )

        val comments = PropertyEntity(
            "comments", "comments",
            PropertyEntity.TaskPropertyType.STRING
        )

        val status = PropertyEntity(
            "status", "status",
            PropertyEntity.TaskPropertyType.ENUM
        )

        val context = PropertyEntity(
            "context", "context",
            PropertyEntity.TaskPropertyType.ENUM
        )

        val startDate = PropertyEntity(
            "startDate", "start_date",
            PropertyEntity.TaskPropertyType.DATE
        )

        val startTime = PropertyEntity(
            "startTime", "start_time",
            PropertyEntity.TaskPropertyType.TIME
        )

        val dueDate = PropertyEntity(
            "dueDate", "due_date",
            PropertyEntity.TaskPropertyType.DATE
        )

        val dueTime = PropertyEntity(
            "dueTime", "due_time",
            PropertyEntity.TaskPropertyType.TIME
        )

        val done = PropertyEntity(
            "done", "done",
            PropertyEntity.TaskPropertyType.BOOLEAN
        )

        val list = listOf(
            title, comments, status, context,
            startDate, startTime, dueDate, dueTime,
            done
        )
    }

    object TaskStatusValues {
        val nextAction = TaskEnumPropertyValueEntity(
            "status",
            TaskEnumPropertyValueEntity.ValueType.RESOURCE,
            "next_action"
        )

        val waiting = TaskEnumPropertyValueEntity(
            "status",
            TaskEnumPropertyValueEntity.ValueType.RESOURCE,
            "waiting"
        )

        val onHold = TaskEnumPropertyValueEntity(
            "status",
            TaskEnumPropertyValueEntity.ValueType.RESOURCE,
            "on_hold"
        )

        val someday = TaskEnumPropertyValueEntity(
            "status",
            TaskEnumPropertyValueEntity.ValueType.RESOURCE,
            "someday"
        )

        val planning = TaskEnumPropertyValueEntity(
            "status",
            TaskEnumPropertyValueEntity.ValueType.RESOURCE,
            "planning"
        )

        val list = listOf(
            nextAction, waiting, onHold, someday, planning
        )
    }

    object TaskContextValues {
        val home = TaskEnumPropertyValueEntity(
            "status",
            TaskEnumPropertyValueEntity.ValueType.RESOURCE,
            "home"
        )

        val errands = TaskEnumPropertyValueEntity(
            "status",
            TaskEnumPropertyValueEntity.ValueType.RESOURCE,
            "errands"
        )

        val work = TaskEnumPropertyValueEntity(
            "status",
            TaskEnumPropertyValueEntity.ValueType.RESOURCE,
            "work"
        )

        val list = listOf(
            home, errands, work
        )
    }

    suspend fun init() {
        TaskProperty.list.forEach { taskPropertyRepository.insertTaskProperty(it) }
        TaskStatusValues.list.forEach {taskEnumPropertyValueRepository.insertTaskEnumPropertyValue(it) }
        TaskContextValues.list.forEach {taskEnumPropertyValueRepository.insertTaskEnumPropertyValue(it) }

        taskFilterRepository

    }

    object GroupBy {
        val status: Grouper<TaskEntity> = PropertyGrouper(TaskProperty.status, "None")
        val context: Grouper<TaskEntity> = PropertyGrouper(TaskProperty.context, "None")
        val dueDate: Grouper<TaskEntity> = DueGrouper()
        val list: Array<Grouper<TaskEntity>> = arrayOf(status, context, dueDate)
    }

    object TaskView {

        val hotlist by lazy {
            hu.botagergo.todolist.feature_task_view.data.model.TaskView.Builder("Hotlist")
                .filter(
                    AndFilter(
                        PropertyFilter(TaskProperty.done, Equals(), true).apply {  },
                        PropertyFilter(
                            TaskProperty.dueDate,
                            LessEqual(allowNull = true),
                            { LocalDate.now().plusDays(3) }
                        )
                    ))
                .build()
        }

        val allGroupedByStatus by lazy {
            hu.botagergo.todolist.feature_task_view.data.model.TaskView.Builder("All Tasks")
                .description("Show all tasks grouped by status")
                .grouper(
                    GroupBy.status
                )
                .sorter(
                    ManualTaskSorter()
                )
                .build()
        }

        val nextAction by lazy {
            hu.botagergo.todolist.feature_task_view.data.model.TaskView.Builder("Next Action")
                .description("Show tasks with status 'Next Action'")
                .filter(
                    AndFilter(
                        PropertyFilter(
                            TaskProperty.status,
                            Equals(),
                            TaskStatusValues.nextAction
                        ),
                        PropertyFilter(TaskProperty.done, Equals(), true),
                        AndFilter(
                            PropertyFilter(
                                TaskProperty.status,
                                Equals(),
                                TaskStatusValues.nextAction
                            ),
                            PropertyFilter(TaskProperty.done, Equals(), true)
                        )

                    )
                )
                .grouper(
                    GroupBy.dueDate
                )
                .sorter(
                    ManualTaskSorter()
                )
                .build()
        }

        val done by lazy {
            hu.botagergo.todolist.feature_task_view.data.model.TaskView.Builder("Done")
                .description("Show completed tasks")
                .filter(
                    PropertyFilter(TaskProperty.done, Equals(), false)
                )
                .sorter(
                    ManualTaskSorter()
                )
                .build()
        }
    }


}