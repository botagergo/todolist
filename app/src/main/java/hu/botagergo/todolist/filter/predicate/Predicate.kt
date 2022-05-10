package hu.botagergo.todolist.filter.predicate

import java.lang.IllegalArgumentException

abstract class Predicate(val kind: PredicateKind) {
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