package hu.botagergo.todolist.util

import android.content.Context

interface NamedByResource {
    val name: Int
    fun toString(context: Context) = context.getString(name)
}