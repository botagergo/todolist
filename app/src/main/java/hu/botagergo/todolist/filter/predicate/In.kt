package hu.botagergo.todolist.filter.predicate

import java.io.Serializable
import java.lang.IllegalArgumentException

class In() : Predicate(PredicateKind.IN), Serializable, Cloneable {
    override fun evaluate(op1: Any?, op2: Any?): Boolean {
        val op2Set = op2 as? Set<*> ?: throw IllegalArgumentException()
        return op2Set.contains(op1)
    }

}