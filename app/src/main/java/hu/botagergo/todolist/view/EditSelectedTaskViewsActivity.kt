package hu.botagergo.todolist.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.R
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.adapter.task_view_list.TaskViewItem
import hu.botagergo.todolist.adapter.task_view_list.TaskViewListAdapter
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.ActivityEditSelectedTaskViewsBinding
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.TaskView

class EditSelectedTaskViewsActivity
    : AppCompatActivity() {

    val binding: ActivityEditSelectedTaskViewsBinding by lazy {
        ActivityEditSelectedTaskViewsBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: TaskViewListAdapter

    private var selectedViews: ArrayList<TaskView> = ArrayList(config.selectedTaskViews.map {
        config.taskViews[it]!!
    })

    private var availableViews: ArrayList<TaskView> = ArrayList(config.taskViews.values)

    val app: ToDoListApplication by lazy {
        application as ToDoListApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.toolbar.setTitle(R.string.selected_task_views)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        adapter = TaskViewListAdapter(app, this, selectedViews, availableViews)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.getItemTouchHelper().attachToRecyclerView(binding.recyclerView)
        adapter.setOnItemClickListener { item, view ->
            if (item is TaskViewItem) {
                val intent = Intent(applicationContext, EditTaskViewActivity::class.java).also {
                    it.putExtra("uuid", item.view.uuid)
                }
                startActivity(intent)
            }
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