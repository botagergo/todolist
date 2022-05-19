package hu.botagergo.todolist.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.EXTRA_IS_EDIT
import hu.botagergo.todolist.R
import hu.botagergo.todolist.ToDoListApplication
import hu.botagergo.todolist.adapter.task_view_list.TaskViewListAdapter
import hu.botagergo.todolist.databinding.ActivityTaskViewListBinding

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

        adapter = TaskViewListAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.getItemTouchHelper().attachToRecyclerView(binding.recyclerView)

        adapter.refresh()
    }

    override fun onRestart() {
        adapter.refresh()
        super.onRestart()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_item_add -> {
                startActivity(
                    Intent(applicationContext, TaskViewActivity::class.java).apply {
                        putExtra(EXTRA_IS_EDIT, false)
                    }
                )
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}