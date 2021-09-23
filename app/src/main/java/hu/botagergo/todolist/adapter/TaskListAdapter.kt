package hu.botagergo.todolist.adapter

import android.app.Activity
import android.content.res.Resources
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
import hu.botagergo.todolist.Configuration
import hu.botagergo.todolist.R
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.view_model.TaskListViewModel


class TaskListAdapter(
    activity: Activity,
    tasks: List<Task>,
    viewModel: TaskListViewModel,
    taskListView: Configuration.TaskListView,
    val parent: Adapter? = null,
    val onItemSizeChanged: (() -> Unit)? = null
) : Adapter(activity, tasks, viewModel, taskListView) {

    lateinit var filteredTasks: ArrayList<Task>
    lateinit var sortedTasks: ArrayList<Task>

    var selectedTaskIndex: Int = -1

    init {
        refreshAll()
    }

    override fun refreshAll() {
        filteredTasks = ArrayList<Task>().apply {
            addAll(tasks)
        }
        taskListView.filter.value?.apply(filteredTasks)

        sortedTasks = ArrayList<Task>().apply {
            addAll(filteredTasks)
        }
        taskListView.sorter.value?.sort(sortedTasks)

        notifyDataSetChanged()
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var task: Task
        val cardView: CardView = itemView.findViewById(R.id.cardView)

        val textViewTitle: TextView = itemView.findViewById(R.id.textView_title)
        val textViewStatus: TextView = itemView.findViewById(R.id.textView_status)
        val textViewContext: TextView = itemView.findViewById(R.id.textView_context)
        val textViewComments: TextView = itemView.findViewById(R.id.textView_comments)
        val layoutActions: View = itemView.findViewById(R.id.linearLayout)

        val imageButtonDone: ImageButton = itemView.findViewById(R.id.imageButton_done)
        val imageButtonEdit: ImageButton = itemView.findViewById(R.id.imageButton_edit)
        val imageButtonDelete: ImageButton = itemView.findViewById(R.id.imageButton_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(inflater.inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val taskViewHolder = viewHolder as TaskViewHolder
        val task = sortedTasks[position]

        val taskSelected = position == selectedTaskIndex

        taskViewHolder.task = task

        taskViewHolder.textViewTitle.text = task.title

        taskViewHolder.textViewStatus.text = task.status.value
        taskViewHolder.textViewStatus.setTextColor(
            getColorForStatus(taskViewHolder.textViewStatus.resources, task.status)
        )
        taskViewHolder.textViewContext.text = if (task.context != Task.Context.None)
            task.context.value else ""

        taskViewHolder.textViewComments.text = task.comments
        taskViewHolder.textViewComments.visibility = if (taskSelected) View.VISIBLE else View.GONE
        if (task.comments.isEmpty()) {
            taskViewHolder.textViewComments.visibility = View.GONE
        }

        taskViewHolder.cardView.background = getColor(
            if (task.done) R.color.task_done_background else R.color.task_background
        ).toDrawable()

        if (taskSelected) {
            taskViewHolder.layoutActions.visibility = View.VISIBLE

            taskViewHolder.imageButtonDone.setImageResource(
                if (task.done) R.drawable.ic_undo else R.drawable.ic_done
            )

            taskViewHolder.imageButtonDone.setOnClickListener {
                val pos = viewHolder.absoluteAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    viewModel.updateTask(task.copy(done = !task.done))
                    notifyItemChanged(pos)
                }
            }

            taskViewHolder.imageButtonEdit.setOnClickListener {
                listener?.onTaskClicked(task)
            }

            taskViewHolder.imageButtonDelete.setOnClickListener {
                val pos = viewHolder.absoluteAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    viewModel.deleteTask(task)
                    sortedTasks.remove(task)
                    notifyItemRemoved(pos)
                }
            }
        } else {
            taskViewHolder.layoutActions.visibility = View.GONE
        }

        taskViewHolder.cardView.setOnClickListener {
            val prevInd = selectedTaskIndex
            selectedTaskIndex = if (prevInd == taskViewHolder.absoluteAdapterPosition) {
                -1
            } else {
                taskViewHolder.absoluteAdapterPosition
            }

            if (prevInd != -1) {
                notifyItemChanged(prevInd)
            }

            if (selectedTaskIndex != -1) {
                notifyItemChanged(selectedTaskIndex)
            }

            onItemSizeChanged?.invoke()
        }


        taskViewHolder.cardView.setOnLongClickListener {
            val pos = taskViewHolder.absoluteAdapterPosition
            if (pos != -1) {
                listener?.onTaskLongClicked(it, task)
                true
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return sortedTasks.size
    }

    private fun getDrawable(id: Int): Drawable? {
        return ResourcesCompat.getDrawable(activity.resources, id, null)
    }

    private fun getColor(id: Int): Color {
        return Color.valueOf(ResourcesCompat.getColor(activity.resources, id, null))
    }

    private fun getColorForStatus(resources: Resources, status: Task.Status): Int {
        return ResourcesCompat.getColor(
            resources, when (status) {
                Task.Status.NextAction -> R.color.status_next_action
                Task.Status.Waiting -> R.color.status_waiting
                Task.Status.Planning -> R.color.status_planning
                Task.Status.OnHold -> R.color.status_on_hold
                Task.Status.None -> R.color.status_none
            }, null
        )
    }

}