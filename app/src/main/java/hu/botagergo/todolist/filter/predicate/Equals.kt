package hu.botagergo.todolist.filter.predicate

import java.io.Serializable

class Equals : Predicate(PredicateKind.EQUAL), Serializable, Cloneable {
    override fun evaluate(op1: Any?, op2: Any?): Boolean {
        return op1 == op2
    }
}