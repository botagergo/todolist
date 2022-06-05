package hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate

import hu.botagergo.todolist.core.util.PredicateKind
import java.io.Serializable
import java.lang.IllegalArgumentException

class In() : Predicate(PredicateKind.IN), Serializable, Cloneable {
    override fun evaluate(op1: Any?, op2: Any?): Boolean {
        val op2Set = op2 as? Set<*> ?: throw IllegalArgumentException()
        return op2Set.contains(op1)
    }

}