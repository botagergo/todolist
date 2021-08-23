package com.example.taskmanager

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.taskmanager.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.Method

class MainActivity : AppCompatActivity(),
    AddTaskFragment.TaskListener, EditTaskFragment.Listener, TaskListFragment.Listener {

    private lateinit var viewModel: TaskViewModel
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        viewModel.setSampleData()

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = fragment?.findNavController()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph, findViewById<DrawerLayout>(R.id.drawer_layout))

        toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayShowTitleEnabled(false);
    }

    override fun onAddTask() {
        Log.d("TM-", "onAddTask")
        navController?.navigate(R.id.action_taskListFragment_to_addTaskFragment)
    }

    override fun onAddTaskResult(task: Task) {
        Log.d("TM-", "onAddTaskResult: $task")
        viewModel.addTask(task)
        Snackbar.make(binding.coordinatorLayout, "Task created", LENGTH_SHORT).show()
    }

    override fun onEditTask(task: Task) {
        Log.d("TM-", "onEditTask: $task")
        val bundle = bundleOf("task" to task)
        navController?.navigate(R.id.action_taskListFragment_to_editTaskFragment, bundle)
    }

    override fun onEditTaskResult(task: Task) {
        Log.d("TM-", "onEditTaskResult: $task")
        Snackbar.make(binding.coordinatorLayout, "Task edited", LENGTH_SHORT).show()
    }

    override fun onDoneTask(task: Task) {
        TODO("Not yet implemented")
    }
}