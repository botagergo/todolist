package hu.botagergo.todolist.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.todolist.R
import hu.botagergo.todolist.group.Grouper
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.PropertySorter
import hu.botagergo.todolist.sorter.Sorter
import hu.botagergo.todolist.task_filter.ConjugateTaskFilter
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

class TaskArrayAdapter(
    private var activity: Activity
) : RecyclerView.Adapter<TaskArrayAdapter.MyViewHolder>() {

    private val TYPE_TASK = 0
    private val TYPE_GROUP_NAME = 1

    var tasks: ArrayList<Task> = ArrayList()
        set(value) {
            val newTasks = ArrayList<Task>().apply {
                this.addAll(value)
            }

            field = newTasks
        }

    private var filteredTasks: ArrayList<Task> = ArrayList()
    private var groupedTasks: ArrayList<Task?>? = null
    private var groups: ArrayList<Pair<String, Int>>? = null

    var filter: ConjugateTaskFilter? = null
    var grouper: Grouper<Any, Task>? = null
    var sorter: Sorter<Task>? = null

    fun isTask(position: Int): Boolean {
        return groupedTasks?.get(position) != null
    }

    interface Listener {
        fun onDoneClicked(task: Task, done: Boolean)
        fun onTaskClicked(task: Task)
        fun onTaskLongClicked(anchor: View, task: Task)
    }

    var listener: Listener? = null

    private fun calculateActualTasks() {
        filteredTasks = ArrayList()
        groups = null
        filteredTasks.addAll(tasks)
        filter?.apply(filteredTasks)
        sorter?.sort(filteredTasks)

        if (grouper != null) {
            val grouped = grouper!!.group(filteredTasks)
            groups = ArrayList()
            groupedTasks = ArrayList()

            for (group in grouped) {
                groupedTasks!!.add(null)
                groups!!.add(group.key.toString() to groupedTasks!!.size - 1)
                groupedTasks!!.addAll(group.value)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        calculateActualTasks()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_TASK) {
            TaskViewHolder(inflater.inflate(R.layout.item_task, parent, false))
        } else {
            GroupNameViewHolder(inflater.inflate(R.layout.item_task_group_name, parent, false))
        }
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        logd(this, "onBindViewHolder")

        viewHolder.group = null
        if (groups != null) {
            for (group in groups!!.reversed()) {
                if (group.second <= position) {
                    viewHolder.group = group.first
                    break
                }
            }
        }

        if (viewHolder.itemViewType == TYPE_TASK) {
            val taskViewHolder = viewHolder as TaskViewHolder
            val task = groupedTasks?.get(position) ?: filteredTasks[position]
            taskViewHolder.task = task

            taskViewHolder.textView.text = task.title
            taskViewHolder.textViewStatus.text = task.status.value
            taskViewHolder.textViewContext.text =
                if (task.context != Task.Context.None) task.context.value else ""
            taskViewHolder.textViewComments.text = task.comments

            val color = ResourcesCompat.getColor(
                taskViewHolder.textViewStatus.resources, when (task.status) {
                    Task.Status.NextAction -> R.color.status_next_action
                    Task.Status.Waiting -> R.color.status_waiting
                    Task.Status.Planning -> R.color.status_planning
                    Task.Status.OnHold -> R.color.status_on_hold
                    Task.Status.None -> R.color.status_none
                }, null
            )

            taskViewHolder.textViewStatus.setTextColor(color)

            taskViewHolder.textViewComments.visibility =
                if (task.comments.isEmpty()) View.GONE else View.VISIBLE

            if (task.done) {
                taskViewHolder.cardView.background =
                    getColor(R.color.task_done_background).toDrawable()
                taskViewHolder.imageButton.background =
                    getColor(R.color.task_done_background).toDrawable()
                taskViewHolder.imageButton.setImageDrawable(getDrawable(R.drawable.ic_check_circle))
            } else {
                taskViewHolder.cardView.background = getColor(R.color.task_background).toDrawable()
                taskViewHolder.imageButton.background =
                    getColor(R.color.task_background).toDrawable()
                taskViewHolder.imageButton.setImageDrawable(getDrawable(R.drawable.ic_circle))
            }

            val cardView = taskViewHolder.cardView
            val imageButton = taskViewHolder.imageButton

            cardView.setOnClickListener {
                val pos = taskViewHolder.bindingAdapterPosition
                if (pos != -1) {
                    listener?.onTaskClicked(task)
                }
            }

            cardView.setOnLongClickListener {
                val pos = taskViewHolder.bindingAdapterPosition
                if (pos != -1) {
                    listener?.onTaskLongClicked(it, task)
                    true
                } else {
                    false
                }
            }

            imageButton.setOnClickListener {
                logd(this, "imageButton.onClickListener")
                val pos = taskViewHolder.bindingAdapterPosition
                if (pos != -1) {
                    listener?.onDoneClicked(task, task.done)
                }
            }
        } else {
            val groupNameViewHolder = viewHolder as GroupNameViewHolder
            groupNameViewHolder.textViewGroupName.text = viewHolder.group!!.toString()
        }
    }

    private fun getDrawable(id: Int): Drawable? {
        return ResourcesCompat.getDrawable(activity.resources, id, null)
    }

    private fun getColor(id: Int): Color {
        return Color.valueOf(ResourcesCompat.getColor(activity.resources, id, null))
    }

    override fun getItemCount(): Int {
        return groupedTasks?.size ?: filteredTasks.size
    }

    override fun getItemViewType(position: Int): Int {
        if (groups == null) {
            return TYPE_TASK
        }

        for (group in groups!!) {
            if (group.second == position) {
                return TYPE_GROUP_NAME
            }
        }

        return TYPE_TASK
    }

    open inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var group: Any? = null
    }

    inner class TaskViewHolder(itemView: View) : MyViewHolder(itemView) {
        var task: Task? = null

        val cardView: CardView = itemView.findViewById(R.id.cardView)

        val textView: TextView = itemView.findViewById(R.id.textView_title)
        val textViewStatus: TextView = itemView.findViewById(R.id.textView_status)
        val textViewContext: TextView = itemView.findViewById(R.id.textView_context)
        val textViewComments: TextView = itemView.findViewById(R.id.textView_comments)
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
    }

    inner class GroupNameViewHolder(itemView: View) : MyViewHolder(itemView) {
        val textViewGroupName: TextView = itemView.findViewById(R.id.textView_groupName)
    }

}