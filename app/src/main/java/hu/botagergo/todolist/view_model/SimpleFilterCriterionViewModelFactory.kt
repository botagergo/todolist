package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.config
import hu.botagergo.todolist.filter.CompositeFilter
import hu.botagergo.todolist.filter.Filter
import hu.botagergo.todolist.filter.PropertyFilter
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskView
import java.lang.IllegalArgumentException
import java.util.*

class SimpleFilterCriterionViewModelFactory(val application: Application, val filter: PropertyFilter<Task>) :
    ViewModelProvider.Factory {

    constructor(application: Application, uuid: UUID) : this(application, findFilter(uuid)) {}

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, PropertyFilter::class.java)
            .newInstance(application, filter)
    }

    companion object {
        private fun findFilter(uuid: UUID): PropertyFilter<Task> {
            for (taskView in config.taskViews.values) {
                if (taskView.filter != null) {
                    val filter = findFilter(taskView.filter, uuid)
                    if (filter != null) {
                        return filter
                    }
                }
            }
            throw IllegalArgumentException()
        }

        private fun findFilter(filter: Filter<Task>, uuid: UUID): PropertyFilter<Task>? {
            if (filter is PropertyFilter<*> && filter.uuid == uuid) {
                return filter as PropertyFilter<Task>
            } else if (filter is CompositeFilter<Task>) {
                for (subFilter in filter.filters) {
                    findFilter(subFilter, uuid).also {
                        if (it != null) {
                            return it
                        }
                    }
                }
            }
            return null
        }
    }

}