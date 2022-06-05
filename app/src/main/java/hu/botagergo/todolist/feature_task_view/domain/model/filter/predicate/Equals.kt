package hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate

import hu.botagergo.todolist.core.util.PredicateKind
import java.io.Serializable

class Equals() : Predicate(PredicateKind.EQUAL), Serializable, Cloneable {
    override fun evaluate(op1: Any?, op2: Any?): Boolean {
        return op1 == op2
    }
}