package hu.botagergo.todolist.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import hu.botagergo.todolist.Configuration
import hu.botagergo.todolist.R
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.ActivityMainBinding
import hu.botagergo.todolist.view_model.TaskListViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

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
                viewModel.addSampleData()
                binding.drawerLayout.close()
                true
            }
            R.id.menu_item_delete_all -> {
                viewModel.deleteAll()
                binding.drawerLayout.close()
                true
            }
            R.id.menu_item_reset_config -> {
                config = Configuration.defaultConfig
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