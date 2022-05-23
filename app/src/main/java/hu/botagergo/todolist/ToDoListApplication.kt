package hu.botagergo.todolist

import android.app.Application
import androidx.room.Room
import hu.botagergo.todolist.filter.CompositeFilter
import hu.botagergo.todolist.filter.Filter
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskDao
import hu.botagergo.todolist.model.TaskDatabase

class ToDoListApplication : Application() {

    private val taskDb: TaskDatabase by lazy {
        Room.databaseBuilder(
            this, TaskDatabase::class.java, "task"
        ).fallbackToDestructiveMigration().build()
    }

    lateinit var taskDao: TaskDao

    val taskAddedEvent: Event<Task> = Event()
    val taskChangedEvent: Event<Task> = Event()
    val taskRemovedEvent: Event<Task> = Event()
    val taskDataSetChangedEvent: Event<Unit> = Event()

    override fun onCreate() {
        taskDao = taskDb.taskDao()
        if (!Configuration.load(this)) {
            initConfig()
        }
        super.onCreate()
    }

    fun initConfig() {
        config.taskViews.putAll(
            listOf(
                Predefined.TaskView.nextAction,
                Predefined.TaskView.allGroupedByStatus,
                Predefined.TaskView.done,
                Predefined.TaskView.hotlist
            )
        )

        config.activeTaskViews.addAll(
            listOf(
                Predefined.TaskView.allGroupedByStatus.uuid,
                Predefined.TaskView.hotlist.uuid,
                Predefined.TaskView.done.uuid
            )
        )

        initFilters(config)
    }

    private fun initFilters(config: Configuration) {
        config.taskViews.forEach { taskView ->
            val filter = taskView.value.filter
            if (filter != null) {
                initFilters(config, filter)
            }
        }
    }

    private fun initFilters(config: Configuration, filter: Filter<Task>) {
        config.taskFilters[filter.uuid] = filter
        if (filter is CompositeFilter<Task>) {
            filter.filters.forEach { childFilter ->
                initFilters(config, childFilter)
            }
        }
    }

}