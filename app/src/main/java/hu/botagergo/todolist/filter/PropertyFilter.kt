package hu.botagergo.todolist.filter

import hu.botagergo.todolist.filter.predicate.Predicate
import hu.botagergo.todolist.util.Property
import java.io.Serializable
import java.util.*

class PropertyFilter<T>(
    var property: Property<T>,
    var predicate: Predicate,
    var operand: Any?,
    var negate: Boolean=false,
    uuid: UUID?=null
) : Filter<T>(uuid), Serializable, Cloneable {

    override fun include(t: T): Boolean {
        val func = operand as? (() -> Any?)
        val operandValue = if (func != null) func() else operand
        return predicate.evaluate(property.getValue(t), operandValue)
    }

    override fun clone(): Filter<T> {
        return PropertyFilter(property, predicate, operand, negate, uuid)
    }

}