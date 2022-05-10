package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.botagergo.todolist.filter.*
import hu.botagergo.todolist.filter.predicate.Predicate
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.util.Property
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class SimpleFilterCriterionViewModel(val app: Application, val filter: PropertyFilter<Task>) : ViewModel() {

    val property: MutableLiveData<Property<Task>?> = MutableLiveData(filter.property)
    val predicate: MutableLiveData<Predicate?> = MutableLiveData(filter.predicate)
    val operand: MutableLiveData<Any?> = MutableLiveData(filter.operand)
    val negate: MutableLiveData<Boolean> = MutableLiveData(filter.negate)
    val uuid: UUID = filter.uuid

    fun save() {
        logd(this, "Updating filter: $filter.uuid")

        if (property.value == null
            || predicate.value == null) {
            throw IllegalArgumentException()
        }

        filter.property = property.value!!
        filter.predicate = predicate.value!!
        filter.operand = operand.value
        filter.negate = negate.value!!
    }

}