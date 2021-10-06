package hu.botagergo.todolist.sorter

import kotlin.reflect.KProperty1

class PropertySorter<T, K>(
    private val property: KProperty1<T, K>,
    private val comp: Comparator<K>
) : Sorter<T> {
    override fun sort(items: ArrayList<T>) {
        items.sortWith { i1, i2 ->
            val p1 = property.get(i1)
            val p2 = property.get(i2)
            comp.compare(p1, p2)
        }
    }
}