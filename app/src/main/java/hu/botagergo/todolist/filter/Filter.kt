package hu.botagergo.todolist.filter

import java.io.Serializable

abstract class Filter<T> : Serializable {
    abstract fun include(t: T): Boolean

    fun apply(tasks: ArrayList<T>) {
        tasks.removeIf {
            !include(it)
        }
    }

}