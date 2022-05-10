package hu.botagergo.todolist.util

import androidx.room.TypeConverter
import hu.botagergo.todolist.filter.Filter
import hu.botagergo.todolist.filter.predicate.PredicateKind
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KProperty1

abstract class Property<T>(val value: Int, private val prop: KProperty1<T, Any?>): NamedByResource, UUIDOwner(), Serializable {

    fun <K> getValue(t: T): K {
        return prop.get(t) as K
    }

    abstract fun supportedPredicates(): Array<PredicateKind>

    override fun getName(): Int {
        logd(this, "value = $value")
        return value
    }

}

object EnumValueIntConverter {
    @TypeConverter
    fun toEnumValue(value: Int?): EnumValue? {
        return if (value == null) {
            null
        } else {
            EnumValue(value)
        }
    }

    @TypeConverter
    fun toInt(enumValue: EnumValue?): Int? {
        return enumValue?.value
    }
}

open class EnumValue internal constructor(val value: Int): NamedByResource, Serializable {

    override fun getName(): Int = value

    override fun equals(other: Any?): Boolean {
        return getName() == (other as? EnumValue)?.getName()
    }

    override fun hashCode(): Int {
        return value
    }

}

class EnumProperty<T>(name: Int, prop: KProperty1<T, Any?>) : Property<T>(name, prop) {

    private val _valuesById: MutableMap<Int, EnumValue> = HashMap()
    private var _values: Array<EnumValue> = arrayOf()

    fun registerValue(value: Int) {
        val enumValue = EnumValue(value)
        _values = _values.plus(enumValue)
        _values[value] = enumValue
    }

    fun registerValues(vararg values: Int) {
        _values += values.map { value -> EnumValue(value) }
        for (value in _values) {
            _valuesById[value.value] = value
        }
    }

    fun values(): Array<EnumValue> = _values

    fun valueOf(id: Int): EnumValue {
        return _valuesById[id] ?: throw IllegalArgumentException()
    }

    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(
        PredicateKind.EXISTS, PredicateKind.EQUAL, PredicateKind.IN
    )

}

class TextProperty<T>(value: Int, prop: KProperty1<T, String?>) : Property<T>(value, prop) {
    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(
        PredicateKind.EQUAL, PredicateKind.IN, PredicateKind.CONTAINS, PredicateKind.LIKE, PredicateKind.REGEX
    )
}

class BooleanProperty<T>(value: Int, prop: KProperty1<T, Boolean?>) : Property<T>(value, prop) {
    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(PredicateKind.EQUAL)
}

class DateProperty<T>(value: Int, prop: KProperty1<T, LocalDate?>) : Property<T>(value, prop) {
    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(
        PredicateKind.EXISTS,
        PredicateKind.EQUAL, PredicateKind.LESS_EQUAL, PredicateKind.LESS,
        PredicateKind.GREATER, PredicateKind.GREATER_EQUAL,
        PredicateKind.IN, PredicateKind.BETWEEN
    )
}

class TimeProperty<T>(value: Int, prop: KProperty1<T, LocalTime?>) : Property<T>(value, prop) {
    override fun supportedPredicates(): Array<PredicateKind> = arrayOf(
        PredicateKind.EXISTS,
        PredicateKind.EQUAL, PredicateKind.LESS_EQUAL, PredicateKind.LESS,
        PredicateKind.GREATER, PredicateKind.GREATER_EQUAL,
        PredicateKind.IN, PredicateKind.BETWEEN
    )}