package hu.botagergo.taskmanager

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.taskmanager.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {
    private lateinit var adapter: TaskArrayAdapter
    private lateinit var viewModel: TaskViewModel
    private lateinit var binding: FragmentTaskListBinding
    private var listener: Listener? = null

    interface Listener {
        fun onAddTask()
        fun onEditTask(task: Task)
        fun onDoneTask(task: Task, done: Boolean)
        fun onDeleteTask(task: Task)
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

        adapter = TaskArrayAdapter(viewModel.getTasks().value!!, activity as FragmentActivity)
        adapter.listener = object : TaskArrayAdapter.Listener {
            override fun onDoneClicked(task: Task, done: Boolean) {
                listener?.onDoneTask(task, done)
            }

            override fun onTaskClicked(task: Task) {
                listener?.onEditTask(task)
            }

            override fun onTaskLongClicked(anchor: View, task: Task) {
                val popupMenu = PopupMenu(context!!, anchor)
                popupMenu.menuInflater.inflate(R.menu.menu_task_list_popup, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    if (it.itemId == R.id.menu_item_delete) {
                        listener?.onDeleteTask(task)
                        true
                    } else {
                        false
                    }
                }
                popupMenu.show()
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
}