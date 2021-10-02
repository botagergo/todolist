package hu.botagergo.todolist.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import hu.botagergo.todolist.*
import hu.botagergo.todolist.databinding.ActivityMainBinding
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.view_model.TaskListViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: TaskListViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val app by lazy { application as ToDoListApplication }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        logd(this, "onCreate")
        super.onCreate(savedInstanceState)

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
        app.storeConfig()
        super.onPause()
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
            R.id.menu_item_add_sample_data -> {
                viewModel.addSampleData()
                true
            }
            R.id.menu_item_delete_all -> {
                viewModel.deleteAll()
                true
            }
            else -> {
                false
            }
        }
    }

}