package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Section
import com.xwray.groupie.TouchCallback
import hu.botagergo.todolist.*
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.FragmentTaskListBinding
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.view_model.TaskListViewModel

import hu.botagergo.todolist.adapter.Adapter
import hu.botagergo.todolist.adapter.SimpleTaskListAdapter
import hu.botagergo.todolist.adapter.TaskItem
import hu.botagergo.todolist.adapter.createAdapter
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.TaskReorderableSorter

class TaskListFragment(private val taskListView: Configuration.TaskListView)
    : Fragment() {
    private lateinit var adapter: Adapter
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by activityViewModels()
    private val app: ToDoListApplication by lazy { requireActivity().application as ToDoListApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        logd(this, "onCreate")
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        //adapter.tasks = viewModel.getTasks().value!!
        //binding.recyclerView.adapter = adapter
        //binding.recyclerView.layoutManager = LinearLayoutManager(context)
        //(binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        //adapter.refreshAll()
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logd(this, "onCreateView")
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logd(this, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        adapter = createAdapter(this.requireActivity().application as ToDoListApplication, viewModel.getTasks().value!!, taskListView)

        adapter.setOnItemDoneClickedListener {
            viewModel.updateTask(it.copy(done = !it.done))
        }

        adapter.setOnItemClickedListener {
            val bundle = bundleOf("uid" to it.uid)
            findNavController().navigate(R.id.action_taskListFragment_to_editTaskFragment, bundle)
        }

        adapter.setOnItemDeleteClickedListener {
            viewModel.deleteTask(it)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        adapter.refresh()



        binding.recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(adapter.getTouchCallback())
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_add) {
            findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
            return true
            R.id.action_taskListFragment_to_editTaskFragment
        }
        return false
    }

}