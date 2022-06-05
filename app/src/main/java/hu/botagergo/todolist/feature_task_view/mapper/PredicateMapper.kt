package hu.botagergo.todolist.feature_task_view.mapper

import android.content.Context
import hu.botagergo.todolist.core.util.PredicateKind
import hu.botagergo.todolist.feature_task_view.data.model.Predicate
import hu.botagergo.todolist.feature_task_view.data.model.PredicateEntity
import hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate.Equals
import hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate.LessEqual

fun PredicateEntity.toPredicateInfo(context: Context): Predicate {
    val resourceId = context.resources.getIdentifier(resourceName, "string", context.packageName)
    return Predicate(
        predicateKind,
        resourceName,
        context.getString(resourceId),
        id
    )
}

fun Predicate.toPredicateEntity(): PredicateEntity {
    return PredicateEntity(predicateKind, resourceName, id)
}

fun PredicateEntity.toPredicate(): hu.botagergo.todolist.feature_task_view.domain.model.filter.predicate.Predicate {
    return when(this.predicateKind) {
        PredicateKind.EXISTS -> TODO()
        PredicateKind.EQUAL ->  Equals()
        PredicateKind.LESS -> TODO()
        PredicateKind.LESS_EQUAL -> LessEqual()
        PredicateKind.GREATER -> TODO()
        PredicateKind.GREATER_EQUAL -> TODO()
        PredicateKind.IN -> TODO()
        PredicateKind.CONTAINS -> TODO()
        PredicateKind.BETWEEN -> TODO()
        PredicateKind.LIKE -> TODO()
        PredicateKind.REGEX -> TODO()
    }
}