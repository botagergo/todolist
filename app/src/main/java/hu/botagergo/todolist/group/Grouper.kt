package hu.botagergo.todolist.group

import hu.botagergo.todolist.util.NamedByResource
import java.io.Serializable

interface Grouper<T, K> : NamedByResource, Serializable, Cloneable {
    fun group(items: List<T>, order: MutableList<K>? = null): MutableList<Pair<K, List<T>>>
    public override fun clone(): Grouper<T, K>
}