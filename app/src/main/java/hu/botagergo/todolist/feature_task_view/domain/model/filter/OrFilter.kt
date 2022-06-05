package hu.botagergo.todolist.feature_task_view.domain.model.filter

class OrFilter<T>(id: Long, vararg filters: Filter<T>) : CompositeFilter<T>(id, *filters) {

    override val filters: MutableList<Filter<T>> = filters.toMutableList()

    override fun include(t: T): Boolean {
        return filters.any {
            it.include(t)
        }
    }

    override fun clone(): Filter<T> {
        return OrFilter(id, *filters.map { it.clone() }.toTypedArray())
    }

    override fun addFilter(filter: Filter<T>) {
        filters
    }

}