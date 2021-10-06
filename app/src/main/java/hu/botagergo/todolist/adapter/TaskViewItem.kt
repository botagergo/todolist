package hu.botagergo.todolist.adapter

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.R
import hu.botagergo.todolist.TaskView
import hu.botagergo.todolist.databinding.ItemTaskViewBinding

class TaskViewItem(val adapter: TaskViewListAdapter, val view: TaskView, private val selected: Boolean) :
    BindableItem<ItemTaskViewBinding>() {

    override fun bind(viewBinding: ItemTaskViewBinding, position: Int) {
        viewBinding.data = this
    }

    override fun getLayout() = R.layout.item_task_view
    override fun getDragDirs() = ItemTouchHelper.UP or ItemTouchHelper.DOWN

    fun onClick() {
        adapter.onItemClick(this)
    }

    fun getIcon(): Drawable? {
        return AppCompatResources.getDrawable(
            adapter.app,
            if (selected)
                R.drawable.ic_remove_selected_view
            else
                R.drawable.ic_add_selected_view
        )
    }

}