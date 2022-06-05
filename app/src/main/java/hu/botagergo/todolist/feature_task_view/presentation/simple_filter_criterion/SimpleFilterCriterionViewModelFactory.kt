package hu.botagergo.todolist.feature_task_view.presentation.simple_filter_criterion

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.botagergo.todolist.feature_task_view.domain.model.filter.CompositeFilter
import hu.botagergo.todolist.feature_task_view.domain.model.filter.Filter
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task_view.data.model.TaskView
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import java.lang.IllegalArgumentException
import java.util.*

class SimpleFilterCriterionViewModelFactory(
    val app: Application, taskViewRepo: TaskViewRepository, val uuid: UUID?, taskViewUuid: UUID?, parentUuid: UUID?
) : ViewModelProvider.Factory {

    val filter = if (uuid != null) findFilter(taskViewRepo, uuid) else null
    val taskView = if (taskViewUuid != null) taskViewRepo.get(taskViewUuid) else null
    private val parentFilter = if (parentUuid != null) findFilter(taskViewRepo, parentUuid) else null

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, Filter::class.java, TaskView::class.java, CompositeFilter::class.java)
            .newInstance(app, filter, taskView, parentFilter)
    }

    companion object {
        fun findFilter(taskViewRepo: TaskViewRepository, uuid: UUID): Filter<TaskEntity> {
            for (taskView in taskViewRepo.getAll()) {
                if (taskView.filter != null) {
                    val filter = findFilter(taskView.filter!!, uuid)
                    if (filter != null) {
                        return filter
                    }
                }
            }
            throw IllegalArgumentException()
        }

        fun findFilter(filter: Filter<TaskEntity>, uuid: UUID): Filter<TaskEntity>? {
            if (filter.uuid == uuid) {
                return filter
            } else if (filter is CompositeFilter<TaskEntity>) {
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