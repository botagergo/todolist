package hu.botagergo.todolist.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.adapter.StatusListAdapter
import hu.botagergo.todolist.databinding.DialogSelectStatusBinding
import hu.botagergo.todolist.model.Task


class SelectStatusDialog(context: Context) : Dialog(context), StatusListAdapter.Listener {

    var selectedStatus: Task.Status? = null

    val binding: DialogSelectStatusBinding by lazy {
        DialogSelectStatusBinding.inflate(layoutInflater, null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)

        val adapter = StatusListAdapter(context)
        adapter.listener = this
        binding.recyclerViewStatusList.adapter = adapter
        binding.recyclerViewStatusList.layoutManager = LinearLayoutManager(context)

        val params: ViewGroup.LayoutParams = window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        window!!.attributes = params as WindowManager.LayoutParams

        super.onCreate(savedInstanceState)
    }

    override fun onStatusClicked(status: Task.Status) {
        selectedStatus = status
        dismiss()
    }

}