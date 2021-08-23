package com.example.taskmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {
    private lateinit var adapter : TaskArrayAdapter
    private lateinit var viewModel: TaskViewModel
    private lateinit var binding: FragmentTaskListBinding
    private var listener: Listener? = null

    inner class TaskArrayAdapterListener(private val viewModel: TaskViewModel) : TaskArrayAdapter.Listener {
        override fun onDoneClicked(task: Task) {
            listener?.onDoneTask(task)
        }

        override fun onTaskClicked(task: Task) {
            listener?.onEditTask(task)
        }

        override fun onTaskLongClicked(anchor: View, task: Task) {
            val popupMenu = PopupMenu(context!!, anchor)
            popupMenu.menuInflater.inflate(R.menu.navigation_view_menu, popupMenu.menu)
            popupMenu.show()
        }
    }

    interface Listener {
        fun onAddTask()
        fun onEditTask(task: Task)
        fun onDoneTask(task: Task)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this.requireActivity()).get(TaskViewModel::class.java)

        adapter = TaskArrayAdapter(viewModel.getTasks().value!!)
        adapter.listener = TaskArrayAdapterListener(viewModel)

        adapter.listener = object : TaskArrayAdapter.Listener {
            override fun onDoneClicked(task: Task) {
                listener?.onDoneTask(task)
            }

            override fun onTaskClicked(task: Task) {
                listener?.onEditTask(task)
            }

            override fun onTaskLongClicked(anchor: View, task: Task) {
                TODO("Not yet implemented")
            }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)

        viewModel.getTasks().observe(viewLifecycleOwner, {
            Log.d("TM-", "TaskViewModel changed")
            adapter.setTasks(viewModel.getTasks().value!!)
        })

        binding.floatingActionButton.setOnClickListener {
            listener?.onAddTask()
        }

    }

    private fun addTask(task: Task) {
        val intent: Intent = Intent(this.context, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK_TITLE, task.title)
            putExtra(EXTRA_TASK_COMMENTS, task.comments)
            putExtra(EXTRA_TASK_STATUS, task.status)
            putExtra(EXTRA_TASK_UID, task.uid)
        }
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult")
        if (requestCode == 0 && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                val task = Task(
                    data.getStringExtra(EXTRA_TASK_TITLE) ?: String(),
                    data.getStringExtra(EXTRA_TASK_COMMENTS) ?: String(),
                    data.extras?.get(EXTRA_TASK_STATUS) as Task.Status
                ).apply {
                    uid = (data.extras?.get(EXTRA_TASK_UID) ?: 0) as Int
                }
                viewModel.addTask(task)
            }
        }
    }}