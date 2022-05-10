@file:Suppress("RedundantCompanionReference", "RedundantCompanionReference",
    "RedundantCompanionReference", "RedundantCompanionReference", "RedundantCompanionReference",
    "RedundantCompanionReference"
)

package hu.botagergo.todolist

import android.content.Context
import androidx.databinding.ObservableArrayList
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.log.logi
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.TaskView
import hu.botagergo.todolist.util.UUIDObservableMap
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*
import kotlin.collections.HashSet

class Configuration : Serializable {

    var taskViews: UUIDObservableMap<TaskView> = UUIDObservableMap()
    var selectedTaskViews: ObservableArrayList<UUID> = ObservableArrayList()

    class State : Serializable {
        var selectedTaskViewUuid: UUID? = null
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

        fun load(context: Context) {
            logd(this, "load")
            config = defaultConfig
            /*config = try {
                val input = ObjectInputStream(
                    context.openFileInput(configFileName)
                )
                input.readObject() as Configuration
            } catch (e: Exception) {
                logi(this, "Setting default config")
                defaultConfig
            }*/
        }

        val defaultConfig: Configuration
            get() {
                return Configuration().apply {

                    this.taskViews = UUIDObservableMap<TaskView>().apply {
                        putAll(
                            listOf(
                                Predefined.TaskView.nextAction,
                                Predefined.TaskView.allGroupedByStatus,
                                Predefined.TaskView.done,
                                Predefined.TaskView.hotlist
                            )
                        )
                    }

                    this.selectedTaskViews = ObservableArrayList<UUID>().apply {
                        addAll(
                            listOf(
                                Predefined.TaskView.allGroupedByStatus.uuid,
                                Predefined.TaskView.hotlist.uuid,
                                Predefined.TaskView.done.uuid
                            )
                        )
                    }
                }
            }
    }
}

lateinit var config: Configuration