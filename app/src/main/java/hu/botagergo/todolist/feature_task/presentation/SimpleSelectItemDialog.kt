package hu.botagergo.todolist.feature_task.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.core.adapter.SimpleItemListAdapter
import hu.botagergo.todolist.databinding.DialogSimpleSelectItemBinding


class SimpleSelectItemDialog<T>(val title: String, val values: Array<T>, context: Context,
                                private val onItemSelected: ((T) -> Unit)?=null) :
    Dialog(context), SimpleItemListAdapter.Listener<T> {

    var selectedItem: T? = null

    val binding: DialogSimpleSelectItemBinding by lazy {
        DialogSimpleSelectItemBinding.inflate(layoutInflater, null, false)
    }

    constructor(title: Int, values: Array<T>, context: Context, onItemSelected: ((T) -> Unit)?=null)
            : this(context.getString(title), values, context, onItemSelected)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)

        binding.textViewTitle.text = title

        val adapter = SimpleItemListAdapter(values, context)
        adapter.listener = this
        binding.recyclerViewContextList.adapter = adapter
        binding.recyclerViewContextList.layoutManager = LinearLayoutManager(context)

        val params: ViewGroup.LayoutParams = window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        window!!.attributes = params as WindowManager.LayoutParams

        super.onCreate(savedInstanceState)
    }

    override fun onItemClicked(item: T) {
        selectedItem = item
        onItemSelected?.invoke(item)
        dismiss()
    }

}