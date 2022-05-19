package hu.botagergo.todolist.adapter.task_view_list

import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.EXTRA_IS_EDIT
import hu.botagergo.todolist.EXTRA_UUID
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemTaskViewBinding
import hu.botagergo.todolist.model.TaskView
import hu.botagergo.todolist.view.TaskViewActivity

class TaskViewItem(
    val adapter: TaskViewListAdapter,
    val view: TaskView,
    val active: Boolean
) :
    BindableItem<ItemTaskViewBinding>() {

    override fun bind(viewBinding: ItemTaskViewBinding, position: Int) {
        viewBinding.data = this
        viewBinding.cardView.setOnClickListener {
            adapter.context.startActivity(
                Intent(adapter.context, TaskViewActivity::class.java).apply {
                    putExtra(EXTRA_UUID, view.uuid)
                    putExtra(EXTRA_IS_EDIT, true)
                }
            )
        }
        viewBinding.imageButton.setOnClickListener {
            adapter.onButtonClicked(this)
        }
    }

    var buttonVisible: ObservableBoolean = ObservableBoolean(true)

    override fun getLayout() = R.layout.item_task_view
    override fun getDragDirs() = ItemTouchHelper.UP or ItemTouchHelper.DOWN

}