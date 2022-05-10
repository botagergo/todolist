package hu.botagergo.todolist.group

import android.content.Context
import hu.botagergo.todolist.util.NamedByResource
import java.io.Serializable

interface Grouper<T> : NamedByResource, Serializable, Cloneable {
    fun group(items: List<T>, context: Context, order: MutableList<Any?>? = null): MutableList<Pair<Any?, List<T>>>
    public override fun clone(): Grouper<T>
}