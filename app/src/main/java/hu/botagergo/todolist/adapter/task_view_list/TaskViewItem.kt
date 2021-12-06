package hu.botagergo.todolist.adapter.task_view_list

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemTaskViewBinding
import hu.botagergo.todolist.model.TaskView
import hu.botagergo.todolist.view.EditTaskViewActivity

class TaskViewItem(
    val adapter: TaskViewListAdapter,
    val view: TaskView,
    val context: Context
) :
    BindableItem<ItemTaskViewBinding>() {

    override fun bind(viewBinding: ItemTaskViewBinding, position: Int) {
        viewBinding.data = this
        viewBinding.cardView.setOnClickListener {
            val intent = Intent(context, EditTaskViewActivity::class.java)
            intent.putExtra("uuid", view.uuid)
            context.startActivity(intent)
        }
    }

    override fun getLayout() = R.layout.item_task_view
    override fun getDragDirs() = ItemTouchHelper.UP or ItemTouchHelper.DOWN

}