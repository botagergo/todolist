package hu.botagergo.todolist.adapter.task_list

import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemTaskGroupHeaderBinding


class TaskGroupHeaderItem(private val adapter: GroupedTaskListAdapter, val groupName: Any) :
    BindableItem<ItemTaskGroupHeaderBinding>(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

    override fun getDragDirs() = ItemTouchHelper.UP or ItemTouchHelper.DOWN
    override fun getLayout() = R.layout.item_task_group_header

    private fun getIcon(isExpanded: Boolean): Int {
        return if (isExpanded) R.drawable.ic_expanded else R.drawable.ic_collapsed
    }

    fun getGroupNameAsString(): String {
        return if (groupName is Int) adapter.context.getString(groupName) else groupName as String
    }

    override fun bind(viewBinding: ItemTaskGroupHeaderBinding, position: Int) {
        viewBinding.data = this

        viewBinding.imageView.setImageResource(
            getIcon(expandableGroup.isExpanded)
        )

        viewBinding.cardView.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewBinding.imageView.setImageResource(
                getIcon(expandableGroup.isExpanded)
            )
            adapter.onGroupExpandedChanged(groupName, expandableGroup.isExpanded)
        }
    }

}
