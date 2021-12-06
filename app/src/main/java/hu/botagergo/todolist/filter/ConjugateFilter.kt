package hu.botagergo.todolist.filter

class ConjugateFilter<T>(private vararg val filters: Filter<T>) : Filter<T>() {

    override fun include(t: T): Boolean {
        return filters.all {
            it.include(t)
        }
    }

    override fun clone(): Filter<T> {
        return ConjugateFilter(*filters.map { it.clone() }.toTypedArray())
    }

}