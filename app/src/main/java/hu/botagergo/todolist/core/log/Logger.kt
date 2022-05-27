package hu.botagergo.todolist.core.log

import android.util.Log

fun loge(obj: Any, message: Any?) {
    Log.e("TM-${obj.javaClass.simpleName}", message?.toString() ?: "NULL")
}

fun logi(obj: Any, message: Any?) {
    Log.i("TM-${obj.javaClass.simpleName}", message?.toString() ?: "NULL")
}

fun logd(obj: Any, message: Any?) {
    Log.d("TM-${obj.javaClass.simpleName}", message?.toString() ?: "NULL")
}

