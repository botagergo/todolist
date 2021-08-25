package hu.botagergo.taskmanager

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
import hu.botagergo.taskmanager.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),
    AddTaskFragment.TaskListener, EditTaskFragment.Listener, TaskListFragment.Listener {

    private lateinit var viewModel: TaskViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        viewModel.setSampleData()

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        navController = fragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(::onNavigationItemSelected)
    }

    override fun onAddTask() {
        Log.d("TM-", "onAddTask")
        navController.navigate(R.id.action_taskListFragment_to_addTaskFragment)
    }

    override fun onAddTaskResult(task: Task) {
        Log.d("TM-", "onAddTaskResult: $task")
        viewModel.addTask(task)
        Snackbar.make(binding.coordinatorLayout, "Task created", LENGTH_SHORT).show()
    }

    override fun onEditTask(task: Task) {
        Log.d("TM-", "onEditTask: $task")
        val bundle = bundleOf("task" to task)
        navController.navigate(R.id.action_taskListFragment_to_editTaskFragment, bundle)
    }

    override fun onEditTaskResult(task: Task) {
        Log.d("TM-", "onEditTaskResult: $task")
        Snackbar.make(binding.coordinatorLayout, "Task edited", LENGTH_SHORT).show()
    }

    override fun onDoneTask(task: Task, done: Boolean) {
        Log.d("TM-", "onDoneTask: $task")
        task.done = done
        viewModel.updateTask(task)
        if (done)
            Snackbar.make(binding.coordinatorLayout, "Task done", LENGTH_SHORT).show()    }

    override fun onDeleteTask(task: Task) {
        Log.d("TM-", "onDeleteTask: $task")
        viewModel.deleteTask(task)
        Snackbar.make(binding.coordinatorLayout, "Task deleted", LENGTH_SHORT).show()
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

    private fun onNavigationItemSelected(menuItem: MenuItem) : Boolean {
        Log.d("TM-", "onNavigationItemSelected")
        return when (menuItem.itemId) {
            R.id.menu_item_delete_all -> {
                viewModel.deleteAll()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                Snackbar.make(binding.coordinatorLayout, "All tasks deleted", LENGTH_SHORT).show()
                true
            }
            else -> {
                false
            }
        }
    }
}