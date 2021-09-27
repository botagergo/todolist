package hu.botagergo.todolist.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import hu.botagergo.todolist.R
import kotlinx.android.synthetic.main.item_task_group_header.view.*


class TaskGroupHeaderItem(val groupName: String) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.button_groupName.text = groupName
        viewHolder.root.button_groupName.setOnClickListener {
            this.expandableGroup.onToggleExpanded()
        }
    }

    override fun getDragDirs(): Int {
        return ItemTouchHelper.UP or ItemTouchHelper.DOWN;
    }

    override fun getLayout() = R.layout.item_task_group_header

}
