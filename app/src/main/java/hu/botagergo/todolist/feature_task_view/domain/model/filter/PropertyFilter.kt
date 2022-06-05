package hu.botagergo.todolist.feature_task_view.domain.model.filter

import hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate.Predicate
import hu.botagergo.todolist.core.util.Property
import java.io.Serializable
import java.util.*

class PropertyFilter<T>(
    val property: Property<T>,
    val predicate: Predicate,
    val operand: Any?,
    val negate: Boolean=false,
    id: Long
) : Filter<T>(id), Serializable, Cloneable {

    override fun include(t: T): Boolean {
        val func = operand as? (() -> Any?)
        val operandValue = if (func != null) func() else operand
        return predicate.evaluate(property.getValue(t), operandValue)
    }

    override fun clone(): Filter<T> {
        return PropertyFilter(property, predicate, operand, negate, id)
    }

}