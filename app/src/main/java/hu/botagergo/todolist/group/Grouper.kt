package hu.botagergo.todolist.group

import hu.botagergo.todolist.util.NamedByResource
import java.io.Serializable

interface Grouper<T> : NamedByResource, Serializable, Cloneable {

    class Group<T>(val name: Any, val tasks: MutableList<T>)

    fun group(items: MutableList<T>, order: MutableList<Any?>? = null): List<Group<T>>
    fun key(item: T): Any

    public override fun clone(): Grouper<T>

}