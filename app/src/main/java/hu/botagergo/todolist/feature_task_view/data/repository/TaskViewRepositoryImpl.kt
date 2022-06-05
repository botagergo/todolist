package hu.botagergo.todolist.feature_task_view.data.repository

import android.content.Context
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.feature_task_view.data.model.TaskView
import hu.botagergo.todolist.feature_task_view.domain.TaskViewNotFoundException
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.*
import java.util.*

class TaskViewRepositoryImpl(private var context: Context) : TaskViewRepository {

    private val configFileName = "task_views"
    private var taskViews: MutableMap<UUID, TaskView>

    init {
        try {
            taskViews =
                ObjectInputStream(context.openFileInput(configFileName)).readObject() as HashMap<UUID, TaskView>
        } catch (e: FileNotFoundException) {
            taskViews = HashMap()
            insertAll(
                listOf(
                    Predefined.TaskView.nextAction,
                    Predefined.TaskView.allGroupedByStatus,
                    Predefined.TaskView.done,
                    Predefined.TaskView.hotlist
                )
            )
        }
    }

    override fun get(uuid: UUID): TaskView {
        return taskViews[uuid] ?: throw TaskViewNotFoundException(uuid)
    }

    override fun getAll(): List<TaskView> {
        return taskViews.values.toList()
    }

    private fun doInsert(taskView: TaskView) {
        taskViews[taskView.uuid] = taskView
    }

    override fun insert(taskView: TaskView) {
        doInsert(taskView)
        MainScope().launch { store() }
    }

    override fun insertAll(taskViews: Iterable<TaskView>) {
        taskViews.forEach { doInsert(it) }
        MainScope().launch { store() }
    }

    private fun store() {
        val output = ObjectOutputStream(
            context.openFileOutput(configFileName, 0)
        )
        output.writeObject(taskViews)
        output.close()
    }

}