@file:Suppress("RedundantCompanionReference", "RedundantCompanionReference",
    "RedundantCompanionReference", "RedundantCompanionReference", "RedundantCompanionReference",
    "RedundantCompanionReference"
)

package hu.botagergo.todolist

import android.content.Context
import androidx.databinding.ObservableArrayList
import hu.botagergo.todolist.core.log.logd
import hu.botagergo.todolist.core.log.logi
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*

class Configuration : Serializable {

    var activeTaskViews: ObservableArrayList<UUID> = ObservableArrayList()

    class State : Serializable {
        var selectedTaskViewUuid: UUID? = null
        var taskGroupExpanded: MutableMap<UUID, MutableMap<Any, Boolean>> = hashMapOf()
    }

    var state: State = State()
    var hideViewTabsWhenOneSelected: Boolean = false


    fun store(context: Context) {
        logd(this, "store")
        val output = ObjectOutputStream(
            context.openFileOutput(configFileName, 0)
        )
        output.writeObject(this)
    }

    companion object {
        const val configFileName: String = "config"

        fun load(context: Context): Boolean {
            logd(this, "load")
            return try {
                val input = ObjectInputStream(
                    context.openFileInput(configFileName)
                )
                config = input.readObject() as Configuration
                true
            } catch (e: Exception) {
                logi(this, "Configuration file not found")
                config = Configuration()
                false
            }
        }

    }
}

lateinit var config: Configuration