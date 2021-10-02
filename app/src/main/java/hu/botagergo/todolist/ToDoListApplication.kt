package hu.botagergo.todolist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import hu.botagergo.todolist.group.PropertyGrouper
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.log.logi
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import hu.botagergo.todolist.task_filter.ConjugateTaskFilter
import hu.botagergo.todolist.task_filter.DoneTaskFilter
import hu.botagergo.todolist.task_filter.StatusTaskFilter
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

class ToDoListApplication : Application() {

    lateinit var configuration: Configuration

    val taskAddedEvent: Event<Task> = Event()
    val taskChangedEvent: Event<Task> = Event()
    val taskRemovedEvent: Event<Task> = Event()
    val taskDataSetChangedEvent: Event<Unit> = Event()

    override fun onCreate() {
        configuration = loadConfig()
        super.onCreate()
    }

    fun storeConfig() {
        storeConfig(configuration)
    }

    private val configFileName: String = "config"

    private fun storeConfig(config: Configuration) {
        logd(this, "storeConfig")
        val output = ObjectOutputStream(
            applicationContext.openFileOutput(configFileName, 0)
        )
        output.writeObject(config)
    }

    private fun loadConfig(): Configuration {
        logd(this, "loadConfig")
        return try {
            val input = ObjectInputStream(
                applicationContext.openFileInput(configFileName)
            )
            input.readObject() as Configuration
        } catch (e: Exception) {
            logi(this, "Setting default config")
            defaultConfig
        }
    }

    private val defaultConfig: Configuration = Configuration().apply {
        this.taskListViews = listOf(
            Configuration.TaskListView(
                "Next Action",
                MutableLiveData(
                    ConjugateTaskFilter(
                        DoneTaskFilter(),
                        StatusTaskFilter(setOf(Task.Status.NextAction))
                    )
                ),
                MutableLiveData(PropertyGrouper(Task::context)),
                MutableLiveData(TaskReorderableSorter())

            ),
            Configuration.TaskListView(
                "All",
                MutableLiveData(
                    ConjugateTaskFilter(
                        DoneTaskFilter(showDone = true, showNotDone = true)
                    )
                ), MutableLiveData(PropertyGrouper(Task::status)),
                MutableLiveData(TaskReorderableSorter())
            ),
            Configuration.TaskListView(
                "Done",
                MutableLiveData(
                    ConjugateTaskFilter(
                        DoneTaskFilter(showDone = true, showNotDone = false)
                    )
                ),
                MutableLiveData(null),
                MutableLiveData(TaskReorderableSorter())

            )
        )
    }

}