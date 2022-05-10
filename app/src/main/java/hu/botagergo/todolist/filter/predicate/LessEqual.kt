package hu.botagergo.todolist.filter.predicate

import java.io.Serializable
import java.lang.IllegalArgumentException

class LessEqual(val allowNull: Boolean = false) : Predicate(PredicateKind.LESS_EQUAL), Serializable, Cloneable {

    override fun evaluate(op1: Any?, op2: Any?): Boolean {
        if (allowNull) {
            if (op1 == null) {
                return true
            } else if (op2 == null) {
                return false
            }
        }

        val op1Comp = op1 as? Comparable<Any?> ?: throw IllegalArgumentException()
        val op2Comp = op2 as? Comparable<Any?> ?: throw IllegalArgumentException()

        return op1Comp <= op2Comp
    }

}