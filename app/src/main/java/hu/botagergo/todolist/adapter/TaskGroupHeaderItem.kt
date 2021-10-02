package hu.botagergo.todolist.adapter

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemTaskGroupHeaderBinding


class TaskGroupHeaderItem(private val adapter: GroupedTaskListAdapter, val groupName: String) : BindableItem<ItemTaskGroupHeaderBinding>(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

    override fun getDragDirs() = ItemTouchHelper.UP or ItemTouchHelper.DOWN
    override fun getLayout() = R.layout.item_task_group_header

    private fun getIcon(isExpanded: Boolean) : Drawable? {
        val drawable = getDrawable(
            adapter.application,
            if (isExpanded)
                R.drawable.ic_expanded
            else
                R.drawable.ic_collapsed
        )
        drawable?.setBounds(0, 0, 60, 60)
        return drawable
    }

    override fun bind(viewBinding: ItemTaskGroupHeaderBinding, position: Int) {
        viewBinding.data = this

        viewBinding.buttonGroupName.setCompoundDrawables(
            getIcon(expandableGroup.isExpanded), null, null, null)

        viewBinding.buttonGroupName.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewBinding.buttonGroupName.setCompoundDrawables(
                getIcon(expandableGroup.isExpanded), null, null, null
            )
            adapter.onGroupHeaderClicked(groupName, expandableGroup.isExpanded)
        }
    }

}
