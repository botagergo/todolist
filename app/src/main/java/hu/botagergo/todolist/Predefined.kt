package hu.botagergo.todolist

import hu.botagergo.todolist.filter.*
import hu.botagergo.todolist.filter.predicate.Equals
import hu.botagergo.todolist.filter.predicate.In
import hu.botagergo.todolist.filter.predicate.LessEqual
import hu.botagergo.todolist.group.DueGrouper
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.group.PropertyGrouper
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.PropertySortSubject
import hu.botagergo.todolist.sorter.SortSubject
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import hu.botagergo.todolist.util.*
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
            startDate, startTime, dueDate, dueTime
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

    object SortSubjects {

        val title: PropertySortSubject<Task, String> by lazy {
            PropertySortSubject(Task::title, R.string.title)
        }

        val startDate: PropertySortSubject<Task, LocalDate?> by lazy {
            PropertySortSubject(Task::startDate, R.string.start_date)
        }

        val dueDate: PropertySortSubject<Task, LocalDate?> by lazy {
            PropertySortSubject(Task::dueDate, R.string.due_date)
        }

        val list: Array<SortSubject<Task>> = arrayOf(title, startDate, dueDate)

    }

    object GroupBy {

        val status: Grouper<Task> = PropertyGrouper(TaskProperty.status, "None")

        val context: Grouper<Task> = PropertyGrouper(TaskProperty.context, "None")

        val dueDate: Grouper<Task> = DueGrouper()

        val list: Array<Grouper<Task>> = arrayOf(status, context, dueDate)

    }

    object TaskView {

        val hotlist by lazy {
            hu.botagergo.todolist.model.TaskView.Builder("Hotlist")
                .filter(
                    ConjugateFilter(
                        PropertyFilter(TaskProperty.done, Equals(), true),
                        PropertyFilter(
                            TaskProperty.dueDate,
                            LessEqual(allowNull = true),
                            { LocalDate.now().plusDays(3) }
                        )
                    ))
                .build()
        }

        val allGroupedByStatus by lazy {
            hu.botagergo.todolist.model.TaskView.Builder("All Tasks")
                .description("Show all tasks grouped by status")
                .grouper(
                    GroupBy.status
                )
                .sorter(
                    TaskReorderableSorter()
                )
                .build()
        }

        val nextAction by lazy {
            hu.botagergo.todolist.model.TaskView.Builder("Next Action")
                .description("Show tasks with status 'Next Action'")
                .filter(
                    ConjugateFilter(
                        PropertyFilter(
                            TaskProperty.status,
                            In(),
                            setOf(TaskStatusValues.nextAction)
                        ),
                        PropertyFilter(TaskProperty.done, Equals(), false)
                    )
                )
                .grouper(
                    GroupBy.dueDate
                )
                .sorter(
                    TaskReorderableSorter()
                )
                .build()
        }

        val done by lazy {
            hu.botagergo.todolist.model.TaskView.Builder("Done")
                .description("Show completed tasks")
                .filter(
                    PropertyFilter(TaskProperty.done, Equals(), true)
                )
                .sorter(
                    TaskReorderableSorter()
                )
                .build()
        }
    }

}