package hu.botagergo.todolist.util

import android.content.Context

interface NamedByResource {
    fun getName(): Int
    fun toString(context: Context) = context.getString(getName())
}