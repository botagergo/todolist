package hu.botagergo.todolist.filter

class ConjugateFilter<T>(vararg filters: Filter<T>) : CompositeFilter<T>(*filters) {

    override fun include(t: T): Boolean {
        return filters.all {
            it.include(t)
        }
    }

    override fun clone(): Filter<T> {
        return ConjugateFilter(*filters.map { it.clone() }.toTypedArray())
    }

}