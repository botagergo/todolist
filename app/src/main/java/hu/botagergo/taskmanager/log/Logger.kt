package hu.botagergo.taskmanager.log

import android.util.Log

fun logd(obj: Any, message: Any?) {
    Log.d("TM-${obj.javaClass.simpleName}", message?.toString() ?: "NULL")
}
