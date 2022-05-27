package hu.botagergo.todolist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ToDoListApplication : Application() {

    override fun onCreate() {
        if (!Configuration.load(this)) {
            initConfig()
        }
        super.onCreate()
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