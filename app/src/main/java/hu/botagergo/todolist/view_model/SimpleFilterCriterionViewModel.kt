package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.botagergo.todolist.filter.*
import hu.botagergo.todolist.filter.predicate.Predicate
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskView
import hu.botagergo.todolist.util.Property
import java.lang.IllegalArgumentException
import java.util.*

class SimpleFilterCriterionViewModel(
    val app: Application, val filter: Filter<Task>?,
    val taskView: TaskView?, private val parentFilter: CompositeFilter<Task>?
    ) : ViewModel() {

    val property: MutableLiveData<Property<Task>?> = MutableLiveData()
    val predicate: MutableLiveData<Predicate?> = MutableLiveData()
    val operand: MutableLiveData<Any?> = MutableLiveData()
    val negate: MutableLiveData<Boolean> = MutableLiveData(false)
    val uuid: UUID? = filter?.uuid

    init {
        if (filter is PropertyFilter<Task>) {
            property.value = filter.property
            predicate.value = filter.predicate
            operand.value = filter.operand
            negate.value = filter.negate
        }
    }

    fun save() {
        if (property.value == null
            || predicate.value == null) {
            throw IllegalArgumentException()
        }

        if (filter is PropertyFilter<Task>) {
            filter.property = property.value!!
            filter.predicate = predicate.value!!
            filter.operand = operand.value
            filter.negate = negate.value!!
        }

        parentFilter?.filters?.add(filter!!)

        if (taskView != null) {
            taskView.filter = filter
        }
    }

}