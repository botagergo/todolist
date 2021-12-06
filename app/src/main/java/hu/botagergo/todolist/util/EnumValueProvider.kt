package hu.botagergo.todolist.util

import java.io.Serializable

interface EnumValueProvider : Serializable {
    fun addValue(value: String)
    fun getValues(): Array<String>
}