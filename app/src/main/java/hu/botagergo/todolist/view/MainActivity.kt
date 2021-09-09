package hu.botagergo.todolist.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.util.forEach
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import hu.botagergo.todolist.*
import hu.botagergo.todolist.databinding.ActivityMainBinding
import hu.botagergo.todolist.group.PropertyGrouper
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.task_filter.ConjugateTaskFilter
import hu.botagergo.todolist.view_model.TaskListViewModel
import java.util.*

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

        app.configuration.taskFilter.value = ConjugateTaskFilter()
        app.configuration.taskGrouper.value = PropertyGrouper(Task::status) { p1, p2 ->
            p1.toString().compareTo(p2.toString())
        }
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
            R.id.menu_item_show_status -> {
                onNavViewTaskStatusClicked(this)
                true
            }
            R.id.menu_item_show_context -> {
                onNavViewTaskContextClicked(this)
                true
            }
            R.id.menu_item_group_by -> {
                onNavViewGroupByClicked(menuItem.actionView)
                true
            }
            else -> {
                false
            }
        }
    }


    private fun onNavViewTaskStatusClicked(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        builder.setTitle("Task Status")

        builder.setPositiveButton("OK") { dialogInterface, _ ->
            val dialog = dialogInterface as AlertDialog
            val positions = dialog.listView.checkedItemPositions
            val set: EnumSet<Task.Status> = EnumSet.noneOf(Task.Status::class.java)
            positions.forEach { pos, b ->
                if (b) {
                    logd(this, "true")
                    set.add(Task.Status.values()[pos])
                }
            }
            logd(this, set)
            app.configuration.taskFilter.value?.statusFilter?.showStatus = set
            app.configuration.taskFilter.value = app.configuration.taskFilter.value
            dialogInterface.dismiss()
        }

        val array = BooleanArray(Task.Status.values().size)
        val showStatus = app.configuration.taskFilter.value?.statusFilter?.showStatus
        for (i in Task.Status.values().indices) {
            array[i] = showStatus?.contains(Task.Status.values()[i]) ?: true
        }

        builder.setMultiChoiceItems(
            Task.Status.values().map { it.value }.toTypedArray(),
            array, null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun onNavViewTaskContextClicked(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        builder.setTitle("Task Context")

        builder.setPositiveButton("OK") { dialogInterface, _ ->
            val dialog = dialogInterface as AlertDialog
            val positions = dialog.listView.checkedItemPositions
            val set: EnumSet<Task.Context> = EnumSet.noneOf(Task.Context::class.java)
            positions.forEach { pos, b ->
                if (b) {
                    logd(this, "true")
                    set.add(Task.Context.values()[pos])
                }
            }
            logd(this, set)
            app.configuration.taskFilter.value?.contextFilter?.showContext = set
            app.configuration.taskFilter.value = app.configuration.taskFilter.value
            dialogInterface.dismiss()
        }

        val array = BooleanArray(Task.Context.values().size)
        val showContext = app.configuration.taskFilter.value?.contextFilter?.showContext
        for (i in Task.Context.values().indices) {
            array[i] = showContext?.contains(Task.Context.values()[i]) ?: true
        }

        builder.setMultiChoiceItems(
            Task.Context.values().map { it.value }.toTypedArray(),
            array, null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun onNavViewGroupByClicked(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menu.add(Menu.NONE, 0, Menu.NONE, "Status")
        popupMenu.menu.add(Menu.NONE, 1, Menu.NONE, "Context")
        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == 0) {
                app.configuration.taskGrouper.value = PropertyGrouper(Task::status) {p1, p2 ->
                    p1.toString().compareTo(p2.toString())
                }
            } else if (it.itemId == 1) {
                app.configuration.taskGrouper.value = PropertyGrouper(Task::context) {p1, p2 ->
                    p1.toString().compareTo(p2.toString())
                }
            }
            true
        }

        popupMenu.show()
    }
}