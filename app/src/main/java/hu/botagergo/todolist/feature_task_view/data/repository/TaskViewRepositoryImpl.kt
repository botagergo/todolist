package hu.botagergo.todolist.feature_task_view.data.repository

import android.content.Context
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.feature_task_view.data.TaskView
import hu.botagergo.todolist.feature_task_view.domain.TaskViewNotFoundException
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

class TaskViewRepositoryImpl(private var context: Context) : TaskViewRepository {

    private val configFileName = "task_views"
    private var taskViews: MutableMap<UUID, TaskView>

    init {
        try {
            taskViews =
                ObjectInputStream(context.openFileInput(configFileName)).readObject() as HashMap<UUID, TaskView>
        } catch (e: Exception) {
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

    override fun insert(taskView: TaskView) {
        taskViews[taskView.uuid] = taskView
        MainScope().launch { store() }
    }

    override fun insertAll(taskViews: Iterable<TaskView>) {
        taskViews.forEach { insert(it) }
        MainScope().launch { store() }
    }

    private fun store() {
        val output = ObjectOutputStream(
            context.openFileOutput(configFileName, 0)
        )
        output.writeObject(taskViews)
    }

}