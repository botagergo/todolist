package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.TaskView
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.adapter.TaskViewListAdapter
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.ActivityEditSelectedTaskViewsBinding
import hu.botagergo.todolist.log.logd

class EditSelectedTaskViewsActivity
    : AppCompatActivity(), TaskViewListAdapter.Listener {

    lateinit var binding: ActivityEditSelectedTaskViewsBinding
    private lateinit var adapterSelected: TaskViewListAdapter
    private lateinit var adapterAvailable: TaskViewListAdapter

    val app: ToDoListApplication by lazy {
        application as ToDoListApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSelectedTaskViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val selectedViews = config.taskViews.filter {
            config.selectedTaskViews.contains(it.uuid)
        }

        val availableViews = config.taskViews.filter {
            !selectedViews.contains(it)
        }

        adapterSelected = TaskViewListAdapter(app, selectedViews, true)
        adapterSelected.listener = this
        binding.recyclerViewSelected.adapter = adapterSelected
        binding.recyclerViewSelected.layoutManager = LinearLayoutManager(this)

        adapterAvailable = TaskViewListAdapter(app, availableViews, false)
        adapterAvailable.listener = this
        binding.recyclerViewAvailable.adapter = adapterAvailable
        binding.recyclerViewAvailable.layoutManager = LinearLayoutManager(this)
    }

    override fun onItemClick(view: TaskView, selected: Boolean) {
        if (selected) {
            config.selectedTaskViews.remove(view.uuid)
            adapterAvailable.addView(view)
        } else {
            config.selectedTaskViews.add(view.uuid)
            adapterSelected.addView(view)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        logd(this, "onOptionsItemSelected")

        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}