package hu.botagergo.todolist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import hu.botagergo.todolist.feature_task.data.model.PropertyEntity
import hu.botagergo.todolist.feature_task.domain.repository.TaskPropertyRepository
import javax.inject.Inject

@HiltAndroidApp
class ToDoListApplication : Application() {

    @Inject lateinit var taskPropertyRepository: TaskPropertyRepository

    override fun onCreate() {
        if (!Configuration.load(this)) {
            initConfig()
        }
        super.onCreate()
    }

    private suspend fun initDatabase() {
        taskPropertyRepository.insertTaskProperty(PropertyEntity(
            "title", "title",
            PropertyEntity.TaskPropertyType.STRING
        ))
        taskPropertyRepository.insertTaskProperty(PropertyEntity(
            "comments", "comments",
            PropertyEntity.TaskPropertyType.STRING
        ))
        taskPropertyRepository.insertTaskProperty(PropertyEntity(
            "status", "status",
            PropertyEntity.TaskPropertyType.ENUM
        ))
        taskPropertyRepository.insertTaskProperty(PropertyEntity(
            "context", "context",
            PropertyEntity.TaskPropertyType.ENUM
        ))
        taskPropertyRepository.insertTaskProperty(PropertyEntity(
            "startDate", "start_date",
            PropertyEntity.TaskPropertyType.DATE
        ))
        taskPropertyRepository.insertTaskProperty(PropertyEntity(
            "startTime", "start_time",
            PropertyEntity.TaskPropertyType.TIME
        ))
        taskPropertyRepository.insertTaskProperty(PropertyEntity(
            "dueDate", "due_date",
            PropertyEntity.TaskPropertyType.DATE
        ))
        taskPropertyRepository.insertTaskProperty(PropertyEntity(
            "dueTime", "due_time",
            PropertyEntity.TaskPropertyType.TIME
        ))

    }

    fun initConfig() {
        config.activeTaskViews.addAll(
            listOf(
                Predefined.TaskView.allGroupedByStatus.uuid,
                Predefined.TaskView.hotlist.uuid,
                Predefined.TaskView.done.uuid
            )
        )

        //initFilters(config)
    }

    /*
    private fun initFilters(config: Configuration) {
        taskViewRepo.getAll().forEach { taskView ->
            val filter = taskView.filter
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
    } */

}