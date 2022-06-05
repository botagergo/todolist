package hu.botagergo.todolist.core.util

import android.content.Context

interface NamedByResource {
    val resourceName: String

    var resourceId: Int?

    fun toString(context: Context) {
        if (resourceId == null) {
            resourceId = context.resources.getIdentifier(resourceName, "string", context.packageName)
        }
        context.getString(resourceId!!)
    }
}