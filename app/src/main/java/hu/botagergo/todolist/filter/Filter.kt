package hu.botagergo.todolist.filter

import hu.botagergo.todolist.util.UUIDOwner
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

abstract class Filter<T>(uuid: UUID?=null) : Serializable, Cloneable, UUIDOwner(uuid) {
    abstract fun include(t: T): Boolean

    fun apply(tasks: ArrayList<T>) {
        tasks.removeIf {
            !include(it)
        }
    }

    public abstract override fun clone(): Filter<T>

}