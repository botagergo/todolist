package hu.botagergo.todolist.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.R
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.adapter.task_view_list.TaskViewListAdapter
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.ActivityTaskViewListBinding
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.TaskView

class TaskViewListActivity
    : AppCompatActivity() {

    val binding: ActivityTaskViewListBinding by lazy {
        ActivityTaskViewListBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: TaskViewListAdapter

    val app: ToDoListApplication by lazy {
        application as ToDoListApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.toolbar.setTitle(R.string.task_views)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbar.inflateMenu(R.menu.menu_task_view_list)
        binding.toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }

        adapter = TaskViewListAdapter(app, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.getItemTouchHelper().attachToRecyclerView(binding.recyclerView)
    }

    override fun onStart() {
        adapter.refresh()
        super.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        logd(this, "onOptionsItemSelected")

        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else if (item.itemId == R.id.menu_item_add) {
            val intent = Intent(applicationContext, EditTaskViewActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}