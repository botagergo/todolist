package hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate

import hu.botagergo.todolist.core.util.PredicateKind
import java.io.Serializable
import java.lang.IllegalArgumentException

abstract class Predicate(val kind: PredicateKind): Serializable {
    abstract fun evaluate(op1: Any?, op2: Any?): Boolean

    companion object {
        fun create(kind: PredicateKind): Predicate {
            return when (kind) {
                PredicateKind.LESS_EQUAL -> {
                    LessEqual()
                }
                PredicateKind.EQUAL -> {
                    Equals()
                }
                PredicateKind.IN -> {
                    In()
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
    }
}