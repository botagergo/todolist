package hu.botagergo.todolist.filter

abstract class CompositeFilter<T>(vararg filters: Filter<T>) : Filter<T>() {
    abstract fun addFilter(filter: Filter<T>)
    abstract val filters: MutableList<Filter<T>>
}