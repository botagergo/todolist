package hu.botagergo.todolist.feature_task_view.data.filter

class ConjugateFilter<T>(vararg filters: Filter<T>) : CompositeFilter<T>(*filters) {

    override val filters: MutableList<Filter<T>> = filters.toMutableList()

    override fun include(t: T): Boolean {
        return filters.all {
            it.include(t)
        }
    }

    override fun clone(): Filter<T> {
        return ConjugateFilter(*filters.map { it.clone() }.toTypedArray())
    }

    override fun addFilter(filter: Filter<T>) {
        filters
    }

}