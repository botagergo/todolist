package hu.botagergo.todolist.feature_task_view.domain.model.filter

abstract class CompositeFilter<T>(id: Long, vararg filters: Filter<T>) : Filter<T>(id) {
    abstract fun addFilter(filter: Filter<T>)
    abstract val filters: MutableList<Filter<T>>
}