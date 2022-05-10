package hu.botagergo.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import hu.botagergo.todolist.R
import hu.botagergo.todolist.util.NamedByResource

class SimpleItemListAdapter<T>(val values: Array<T>, val context: Context) :
    RecyclerView.Adapter<SimpleItemListAdapter<T>.ViewHolder>() {

    interface Listener<T> {
        fun onItemClicked(item: T)
    }

    var listener: Listener<T>? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val textView: TextView

        init {
            cardView.setOnClickListener {
                listener?.onItemClicked(values[this.bindingAdapterPosition])
            }
            textView = view.findViewById(R.id.textView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_simple, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (values[position] is NamedByResource) {
            holder.textView.text = (values[position] as NamedByResource).toString(context)
        } else {
            holder.textView.text = values[position].toString()
        }
    }

    override fun getItemCount() = values.size

}