package hu.botagergo.todolist.feature_task_view.domain.model.filter

import hu.botagergo.todolist.core.util.UUIDOwner
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

abstract class Filter<T>(val id: Long) : Serializable, Cloneable {
    abstract fun include(t: T): Boolean

    fun apply(tasks: ArrayList<T>) {
        tasks.removeIf {
            !include(it)
        }
    }

    public abstract override fun clone(): Filter<T>

}