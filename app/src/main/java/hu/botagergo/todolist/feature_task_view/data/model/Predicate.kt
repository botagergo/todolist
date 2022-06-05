package hu.botagergo.todolist.feature_task_view.data.model

import hu.botagergo.todolist.core.util.PredicateKind

data class Predicate(
    val predicateKind: PredicateKind,
    val resourceName: String,
    val displayName: String,
    val id: Long
)