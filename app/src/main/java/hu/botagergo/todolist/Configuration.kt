@file:Suppress("RedundantCompanionReference", "RedundantCompanionReference",
    "RedundantCompanionReference", "RedundantCompanionReference", "RedundantCompanionReference",
    "RedundantCompanionReference"
)

package hu.botagergo.todolist

import android.content.Context
import androidx.databinding.ObservableArrayList
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.log.logi
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*

class Configuration : Serializable {

    var taskViews: ObservableArrayList<TaskView> = ObservableArrayList()
    var selectedTaskViews: ObservableArrayList<UUID> = ObservableArrayList()

    class State : Serializable {
        var selectedTaskViewUuid: UUID? = null
    }

    var state: State = State()

    fun store(context: Context) {
        logd(this, "store")
        val output = ObjectOutputStream(
            context.openFileOutput(configFileName, 0)
        )
        output.writeObject(this)
    }

    companion object {
        const val configFileName: String = "config"

        fun load(context: Context): Configuration {
            logd(this, "load")
            return try {
                val input = ObjectInputStream(
                    context.openFileInput(configFileName)
                )
                input.readObject() as Configuration
            } catch (e: Exception) {
                logi(this, "Setting default config")
                defaultConfig
            }

        }

        val defaultConfig: Configuration
            get() {
                return Configuration().apply {
                    this.taskViews = ObservableArrayList<TaskView>().apply {
                        addAll(
                            listOf(
                                TaskView.Predefined.all,
                                TaskView.Predefined.nextAction,
                                TaskView.Predefined.allGroupedByStatus,
                                TaskView.Predefined.done
                            )
                        )
                    }

                    this.selectedTaskViews = ObservableArrayList<UUID>().apply {
                        addAll(
                            listOf(
                                TaskView.Predefined.allGroupedByStatus.uuid,
                                TaskView.Predefined.nextAction.uuid
                            )
                        )
                    }
                }
            }
    }
}

lateinit var config: Configuration