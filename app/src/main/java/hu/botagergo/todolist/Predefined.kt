package hu.botagergo.todolist

import hu.botagergo.todolist.filter.ConjugateFilter
import hu.botagergo.todolist.filter.PropertyEqualsFilter
import hu.botagergo.todolist.filter.PropertyInFilter
import hu.botagergo.todolist.filter.PropertyLessEqualFilter
import hu.botagergo.todolist.group.DueGrouper
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.group.PropertyGrouper
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.PropertySortSubject
import hu.botagergo.todolist.sorter.SortSubject
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import java.time.LocalDate

class Predefined {

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

        val status: Grouper<Task, Any?> = PropertyGrouper(Task::status, R.string.status, "None")

        val context: Grouper<Task, Any?> = PropertyGrouper(Task::context, R.string.context, "None")

        val dueDate: Grouper<Task, Any?> = DueGrouper()

        val list: Array<Grouper<Task, Any?>> = arrayOf(status, context, dueDate)

    }

    object TaskView {

        val hotlist by lazy {
            hu.botagergo.todolist.model.TaskView.Builder("Hotlist")
                .filter(
                    ConjugateFilter(
                        PropertyEqualsFilter(Task::done, false),
                        PropertyLessEqualFilter(
                            Task::dueDate,
                            { LocalDate.now().plusDays(3) } as (() -> LocalDate))
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
                        PropertyInFilter(Task::status, setOf(Task.Status("Next Action"))),
                        PropertyEqualsFilter(Task::done, false)
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
                    PropertyEqualsFilter(Task::done, true)
                )
                .sorter(
                    TaskReorderableSorter()
                )
                .build()
        }
    }

}