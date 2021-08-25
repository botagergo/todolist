package hu.botagergo.taskmanager

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView

class TaskArrayAdapter(private var tasks: ArrayList<Task>) : RecyclerView.Adapter<TaskArrayAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView_title)
        val textViewStatus: TextView = itemView.findViewById(R.id.textView_status)
        val textViewComments: TextView = itemView.findViewById(R.id.textView_comments)
        val imageButton: ImageButton = itemView.findViewById(R.id.button)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    var listener: Listener? = null

    fun setTasks(tasks: ArrayList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val taskView = inflater.inflate(R.layout.item_task, parent, false)

        val holder = ViewHolder(taskView)

        val cardView = taskView.findViewById<CardView>(R.id.cardView)
        cardView.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onTaskClicked(tasks[pos])
            }
        }

        cardView.setOnLongClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != -1) {
                listener?.onTaskLongClicked(it, tasks[pos])
                true
            } else {
                false
            }
        }

        val checkBox = taskView.findViewById<ImageButton>(R.id.button)
        checkBox.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != -1) {
                Log.d("TM-", "TaskArrayAdapter " + pos.toString())
                listener?.onDoneClicked(tasks[pos], !tasks[pos].done)
            }
        }

        return holder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get the data model based on position
        val task: Task = tasks[position]
        // Set item views based on your views and data model
        viewHolder.textView.text = task.title
        viewHolder.textViewStatus.text = task.status.value
        viewHolder.textViewComments.text = task.comments
        when (task.status) {
            Task.Status.NextAction -> viewHolder.textViewStatus.setTextColor(Color.parseColor("#53B556"))
            Task.Status.Waiting -> viewHolder.textViewStatus.setTextColor(Color.parseColor("#B3BB2F"))
            Task.Status.Planning -> viewHolder.textViewStatus.setTextColor(Color.parseColor("#4662B4"))
            Task.Status.OnHold -> viewHolder.textViewStatus.setTextColor(Color.parseColor("#BF2424"))
        }

        viewHolder.textViewComments.visibility = if (task.comments.isEmpty()) View.GONE else View.VISIBLE

        val resId: Int?
        val background: ColorDrawable

        if (task.done) {
            resId = R.drawable.ic_check_circle
            viewHolder.cardView.background = Color.rgb(180, 237, 171).toDrawable()
            viewHolder.imageButton.background = Color.rgb(180, 237, 171).toDrawable()
        }
        else {
            resId = R.drawable.ic_circle
            viewHolder.cardView.background = Color.WHITE.toDrawable()
            viewHolder.imageButton.background = Color.WHITE.toDrawable()
        }

        val drawable = ResourcesCompat.getDrawable(viewHolder.imageButton.resources, resId, null)
        viewHolder.imageButton.setImageDrawable(drawable)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    interface Listener {
        fun onDoneClicked(task: Task, done: Boolean)
        fun onTaskClicked(task: Task)
        fun onTaskLongClicked(anchor: View, task: Task)
    }
}