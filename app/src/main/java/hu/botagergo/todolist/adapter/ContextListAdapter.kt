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

class ContextListAdapter(val context: Context) :
    RecyclerView.Adapter<ContextListAdapter.ViewHolder>() {

    interface Listener {
        fun onContextClicked(context: Task.Context)
    }

    var listener: Listener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView
        val textView: TextView

        init {
            cardView = view.findViewById(R.id.cardView)
            cardView.setOnClickListener {
                listener?.onContextClicked(Task.Context.values()[this.bindingAdapterPosition])
            }
            textView = view.findViewById(R.id.textView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_context, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = Task.Context.values()[position].value
    }

    override fun getItemCount() = Task.Context.values().size

}