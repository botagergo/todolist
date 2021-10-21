package hu.botagergo.todolist.adapter

import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemTaskViewHeaderBinding

class TaskViewHeaderItem(val name: String) : BindableItem<ItemTaskViewHeaderBinding>() {
    override fun bind(viewBinding: ItemTaskViewHeaderBinding, position: Int) {
        viewBinding.name = name
    }

    override fun getLayout() = R.layout.item_task_view_header

}