package hu.botagergo.todolist.feature_task.presentation.task_list

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.botagergo.todolist.Configuration
import hu.botagergo.todolist.R
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.ActivityTaskListBinding
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository
import hu.botagergo.todolist.feature_task_view.presentation.task_view_list.TaskViewListActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TaskListActivity : AppCompatActivity() {

    @Inject lateinit var taskRepo: TaskRepository

    private lateinit var navController: NavController
    private lateinit var binding: ActivityTaskListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        navController = fragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(::onNavigationItemSelected)
    }

    override fun onPause() {
        config.store(this)
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            true
        } else {
            false
        }
    }

    private fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_item_add_sample_data -> {
                lifecycleScope.launch {
                    taskRepo.addSampleData()
                }
                binding.drawerLayout.close()
                true
            }
            R.id.menu_item_delete_all -> {
                lifecycleScope.launch {
                    taskRepo.deleteAll()
                }
                binding.drawerLayout.close()
                true
            }
            R.id.menu_item_reset_config -> {
                config = Configuration()
                (application as ToDoListApplication).initConfig()
                recreate()
                true
            }
            R.id.menu_item_task_views -> {
                val intent = Intent(this, TaskViewListActivity::class.java)
                binding.drawerLayout.close()
                startActivity(intent)
                true
            }
            else -> {
                false
            }
        }
    }
}
