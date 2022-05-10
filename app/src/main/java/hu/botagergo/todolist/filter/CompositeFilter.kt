package hu.botagergo.todolist.filter

abstract class CompositeFilter<T>(vararg val filters: Filter<T>) : Filter<T>()