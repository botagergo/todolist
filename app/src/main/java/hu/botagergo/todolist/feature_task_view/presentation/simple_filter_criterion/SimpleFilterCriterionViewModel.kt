package hu.botagergo.todolist.feature_task_view.presentation.simple_filter_criterion

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.botagergo.todolist.feature_task_view.domain.model.filter.*
import hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate.Predicate
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.core.util.Property
import java.lang.IllegalArgumentException
import java.util.*

class SimpleFilterCriterionViewModel(
    val app: Application, filter: Filter<TaskEntity>?, private val parentFilter: CompositeFilter<TaskEntity>?
    ) : ViewModel() {

    val property: MutableLiveData<Property<TaskEntity>?> = MutableLiveData()
    val predicate: MutableLiveData<Predicate?> = MutableLiveData()
    val operand: MutableLiveData<Any?> = MutableLiveData()
    val negate: MutableLiveData<Boolean> = MutableLiveData(false)
    val uuid: UUID? = filter?.uuid

    init {
        if (filter is PropertyFilter<TaskEntity>) {
            property.value = filter.property
            predicate.value = filter.predicate
            operand.value = filter.operand
            negate.value = filter.negate
        }
    }

    val filter: PropertyFilter<TaskEntity>
        get() {
            if (property.value == null
                || predicate.value == null) {
                throw IllegalArgumentException()
            }

            return PropertyFilter(property.value!!, predicate.value!!, operand.value)
        }

}