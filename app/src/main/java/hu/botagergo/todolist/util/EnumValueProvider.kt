package hu.botagergo.todolist.util

interface EnumValueProvider {
    fun addValue(value: String)
    fun getValues(): Array<String>
}