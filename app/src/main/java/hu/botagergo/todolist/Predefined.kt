package hu.botagergo.todolist

import hu.botagergo.todolist.core.util.*
import hu.botagergo.todolist.feature_task_view.data.filter.*
import hu.botagergo.todolist.feature_task_view.data.filter.predicate.Equals
import hu.botagergo.todolist.feature_task_view.data.filter.predicate.LessEqual
import hu.botagergo.todolist.feature_task_view.data.group.DueGrouper
import hu.botagergo.todolist.feature_task_view.data.group.Grouper
import hu.botagergo.todolist.feature_task_view.data.group.PropertyGrouper
import hu.botagergo.todolist.feature_task.data.Task
import hu.botagergo.todolist.feature_task_view.data.sorter.ManualTaskSorter
import java.time.LocalDate

class Predefined {

    object TaskProperty {
        val title: TextProperty<Task> = TextProperty(R.string.title, Task::title)
        val comments: TextProperty<Task> = TextProperty(R.string.comments, Task::comments)
        val status: EnumProperty<Task> = EnumProperty(R.string.status, Task::status).apply {
            this.registerValues(
                R.string.next_action,
                R.string.waiting,
                R.string.on_hold,
                R.string.someday,
                R.string.planning,
            )
        }
        val context: EnumProperty<Task> = EnumProperty(R.string.context, Task::context).apply {
            this.registerValues(
                R.string.home,
                R.string.work,
                R.string.errands,
            )
        }
        val startDate: DateProperty<Task> = DateProperty(R.string.start_date, Task::startDate)
        val startTime: TimeProperty<Task> = TimeProperty(R.string.start_time, Task::startTime)
        val dueDate: DateProperty<Task> = DateProperty(R.string.due_date, Task::dueDate)
        val dueTime: TimeProperty<Task> = TimeProperty(R.string.due_time, Task::dueTime)

        val done: BooleanProperty<Task> = BooleanProperty(R.string.done, Task::done)

        val list: Array<Property<Task>> = arrayOf(
            title, comments, status, context,
            startDate, startTime, dueDate, dueTime, done
        )

    }

    object TaskStatusValues {
        val nextAction = TaskProperty.status.valueOf(R.string.next_action)
        val waiting = TaskProperty.status.valueOf(R.string.waiting)
        val onHold = TaskProperty.status.valueOf(R.string.on_hold)
        val someday = TaskProperty.status.valueOf(R.string.someday)
        val planning = TaskProperty.status.valueOf(R.string.planning)
    }

    object TaskContextValues {
        val home = TaskProperty.context.valueOf(R.string.home)
        val work = TaskProperty.context.valueOf(R.string.work)
        val errands = TaskProperty.context.valueOf(R.string.errands)
    }

    object GroupBy {
        val status: Grouper<Task> = PropertyGrouper(TaskProperty.status, "None")
        val context: Grouper<Task> = PropertyGrouper(TaskProperty.context, "None")
        val dueDate: Grouper<Task> = DueGrouper()
        val list: Array<Grouper<Task>> = arrayOf(status, context, dueDate)
    }

    object TaskView {

        val hotlist by lazy {
            hu.botagergo.todolist.feature_task_view.data.TaskView.Builder("Hotlist")
                .filter(
                    ConjugateFilter(
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
            hu.botagergo.todolist.feature_task_view.data.TaskView.Builder("All Tasks")
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
            hu.botagergo.todolist.feature_task_view.data.TaskView.Builder("Next Action")
                .description("Show tasks with status 'Next Action'")
                .filter(
                    ConjugateFilter(
                        PropertyFilter(
                            TaskProperty.status,
                            Equals(),
                            TaskStatusValues.nextAction
                        ),
                        PropertyFilter(TaskProperty.done, Equals(), true),
                        ConjugateFilter(
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
            hu.botagergo.todolist.feature_task_view.data.TaskView.Builder("Done")
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