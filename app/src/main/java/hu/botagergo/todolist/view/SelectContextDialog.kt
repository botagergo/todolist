package hu.botagergo.todolist.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.adapter.ContextListAdapter
import hu.botagergo.todolist.databinding.DialogSelectContextBinding
import hu.botagergo.todolist.model.Task


class SelectContextDialog(context: Context) : Dialog(context), ContextListAdapter.Listener {

    var selectedContext: Task.Context? = null

    val binding: DialogSelectContextBinding by lazy {
        DialogSelectContextBinding.inflate(layoutInflater, null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)

        val adapter = ContextListAdapter(context)
        adapter.listener = this
        binding.recyclerViewContextList.adapter = adapter
        binding.recyclerViewContextList.layoutManager = LinearLayoutManager(context)

        val params: ViewGroup.LayoutParams = window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        window!!.attributes = params as WindowManager.LayoutParams

        super.onCreate(savedInstanceState)
    }

    override fun onContextClicked(context: Task.Context) {
        selectedContext = context
        dismiss()
    }

}