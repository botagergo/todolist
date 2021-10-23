package hu.botagergo.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.todolist.R
import hu.botagergo.todolist.model.Task

class StatusListAdapter(val context: Context) :
    RecyclerView.Adapter<StatusListAdapter.ViewHolder>() {

    interface Listener {
        fun onStatusClicked(status: Task.Status)
    }

    var listener: Listener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView
        val textView: TextView

        init {
            cardView = view.findViewById(R.id.cardView)
            cardView.setOnClickListener {
                listener?.onStatusClicked(Task.Status.values()[this.bindingAdapterPosition])
            }
            textView = view.findViewById(R.id.textView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = Task.Status.values()[position].value
    }

    override fun getItemCount() = Task.Status.values().size

}