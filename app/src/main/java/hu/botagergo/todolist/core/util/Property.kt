package hu.botagergo.todolist.core.util

import java.io.Serializable
import kotlin.reflect.KProperty1

abstract class Property<T>(
    val id: String,
    val resourceName: String,
    val displayName: String,
    private val prop: KProperty1<T, Any?>
    ): Serializable {

    @Suppress("UNCHECKED_CAST")
    fun <K> getValue(t: T): K {
        return prop.get(t) as K
    }

    abstract fun supportedPredicates(): Array<PredicateKind>

    abstract val comparable: Boolean

}

open class EnumValue internal constructor(val id: Int, val displayName: String): Serializable {

    override fun equals(other: Any?): Boolean {
        return id == (other as? EnumValue)?.id
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String = displayName

}

class EnumProperty<T>(id: String, resourceName: String, displayName: String, prop: KProperty1<T, Any?>)
    : Property<T>(id, resourceName, displayName, prop) {

    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(
        PredicateKind.EXISTS, PredicateKind.EQUAL, PredicateKind.IN
    )

    override val comparable: Boolean = false

}

class StringProperty<T>(id: String, resourceName: String, displayName: String, prop: KProperty1<T, String?>)
    : Property<T>(id, resourceName, displayName, prop) {

    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(
        PredicateKind.EQUAL, PredicateKind.IN, PredicateKind.CONTAINS, PredicateKind.LIKE, PredicateKind.REGEX
    )

    override val comparable: Boolean = true

}

class BooleanProperty<T>(id: String, resourceName: String, displayName: String, prop: KProperty1<T, String?>)
    : Property<T>(id, resourceName, displayName, prop) {

    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(PredicateKind.EQUAL)

    override val comparable: Boolean = true

}

class DateProperty<T>(id: String, resourceName: String, displayName: String, prop: KProperty1<T, String?>)
    : Property<T>(id, resourceName, displayName, prop) {

    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(
        PredicateKind.EXISTS,
        PredicateKind.EQUAL, PredicateKind.LESS_EQUAL, PredicateKind.LESS,
        PredicateKind.GREATER, PredicateKind.GREATER_EQUAL,
        PredicateKind.IN, PredicateKind.BETWEEN
    )

    override val comparable: Boolean = true

}

class TimeProperty<T>(id: String, resourceName: String, displayName: String, prop: KProperty1<T, String?>)
    : Property<T>(id, resourceName, displayName, prop) {

    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(
        PredicateKind.EXISTS,
        PredicateKind.EQUAL, PredicateKind.LESS_EQUAL, PredicateKind.LESS,
        PredicateKind.GREATER, PredicateKind.GREATER_EQUAL,
        PredicateKind.IN, PredicateKind.BETWEEN
    )

    override val comparable: Boolean = true

}