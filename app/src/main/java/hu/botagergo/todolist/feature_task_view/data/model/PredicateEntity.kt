package hu.botagergo.todolist.feature_task_view.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.botagergo.todolist.core.util.PredicateKind

@Entity(
    tableName = "predicate"
)
data class PredicateEntity(
    val predicateKind: PredicateKind,
    val resourceName: String,
    @PrimaryKey val id: Long
)