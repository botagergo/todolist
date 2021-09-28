package hu.botagergo.todolist.group

interface Grouper<K, T> {
    fun group(items: List<T>, order: List<K>? = null): MutableList<Pair<K, List<T>>>
}