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
    : AppCompatActivity() {

    lateinit var binding: ActivityEditSelectedTaskViewsBinding
    private lateinit var adapter: TaskViewListAdapter

    var selectedViews: ArrayList<TaskView> = ArrayList(config.taskViews.filter {
        config.selectedTaskViews.contains(it.uuid)
    })
    var availableViews: ArrayList<TaskView> = ArrayList(config.taskViews)

    val app: ToDoListApplication by lazy {
        application as ToDoListApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSelectedTaskViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = TaskViewListAdapter(app, selectedViews, availableViews)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.getItemTouchHelper().attachToRecyclerView(binding.recyclerView)
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