package hu.botagergo.taskmanager.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import hu.botagergo.taskmanager.*
import hu.botagergo.taskmanager.databinding.ActivityMainBinding
import hu.botagergo.taskmanager.log.logd
import hu.botagergo.taskmanager.model.Task
import hu.botagergo.taskmanager.view_model.TaskListViewModel

class MainActivity : AppCompatActivity(),
    AddTaskFragment.TaskListener, EditTaskFragment.Listener, TaskListFragment.Listener {

    private lateinit var viewModel: TaskListViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var taskListFragment: TaskListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        logd(this, "onCreate")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        viewModel.setSampleData()

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        navController = fragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(::onNavigationItemSelected)
    }

    override fun onCreate(taskListFragment: TaskListFragment) {
        this.taskListFragment = taskListFragment
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("TM-", "onOptionsItemSelected")
        return if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            true
        } else {
            false
        }
    }

    private fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        logd(this, "onNavigationItemSelected")
        return when (menuItem.itemId) {
            R.id.menu_item_show_status -> {
                taskListFragment.onNavViewTaskStatusClicked(this)
                true
            }
            R.id.menu_item_show_context -> {
                taskListFragment.onNavViewTaskContextClicked(this)
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onAddTask() {
        Log.d("TM-", "onAddTask")
        navController.navigate(R.id.action_taskListFragment_to_addTaskFragment)
    }

    override fun onAddTaskResult(task: Task) {
        Log.d("TM-", "onAddTaskResult: $task")
        viewModel.addTask(task)
    }

    override fun onEditTask(task: Task) {
        Log.d("TM-", "onEditTask: $task")
        val bundle = bundleOf("uid" to task.uid)
        navController.navigate(R.id.action_taskListFragment_to_editTaskFragment, bundle)
    }

    override fun onEditTaskResult(task: Task) {
        Log.d("TM-", "onEditTaskResult: $task")
        viewModel.updateTask(task)
    }

    override fun onDoneTask(task: Task, done: Boolean) {
        Log.d("TM-", "onDoneTask: $task")
        viewModel.updateTask(task.copy(done = done))
    }

    override fun onDeleteTask(task: Task) {
        Log.d("TM-", "onDeleteTask: $task")
        viewModel.deleteTask(task)
    }
}