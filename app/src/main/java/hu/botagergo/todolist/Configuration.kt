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
import kotlin.collections.HashSet

class Configuration : Serializable {

    var taskViews: ObservableArrayList<TaskView> = ObservableArrayList()
    var selectedTaskViews: ObservableArrayList<UUID> = ObservableArrayList()

    class State : Serializable {
        var selectedTaskViewUuid: UUID? = null
    }

    var state: State = State()

    var hideViewTabsWhenOneSelected: Boolean = false

    class EnumValueProvider : hu.botagergo.todolist.util.EnumValueProvider {

        private val values: MutableSet<String> = HashSet()

        override fun addValue(value: String) {
            values.add(value)
        }

        override fun getValues(): Array<String> {
            return values.toTypedArray()
        }

    }

    var statusValueProvider: EnumValueProvider = EnumValueProvider()
    var contextValueProvider: EnumValueProvider = EnumValueProvider()

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
            config = try {
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

                    this.statusValueProvider = EnumValueProvider().apply {
                        addValue("Next Action")
                        addValue("Planning")
                        addValue("On Hold")
                        addValue("Waiting")
                    }

                    this.contextValueProvider = EnumValueProvider().apply {
                        addValue("Home")
                        addValue("Work")
                        addValue("Errands")
                    }

                    this.taskViews = ObservableArrayList<TaskView>().apply {
                        addAll(
                            listOf(
                                TaskView.Predefined.nextAction,
                                TaskView.Predefined.allGroupedByStatus,
                                TaskView.Predefined.done,
                                TaskView.Predefined.hotlist
                            )
                        )
                    }

                    this.selectedTaskViews = ObservableArrayList<UUID>().apply {
                        addAll(
                            listOf(
                                TaskView.Predefined.allGroupedByStatus.uuid,
                                TaskView.Predefined.hotlist.uuid,
                                TaskView.Predefined.done.uuid
                            )
                        )
                    }
                }
            }
    }
}

lateinit var config: Configuration