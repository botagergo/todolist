package hu.botagergo.todolist.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import hu.botagergo.todolist.R
import kotlinx.android.synthetic.main.item_task_group_header.view.*


class TaskGroupHeaderItem(private val adapter: GroupedTaskListAdapter, val groupName: String) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup


    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.root.button_groupName.setCompoundDrawables(
            getIcon(viewHolder.root.context, expandableGroup.isExpanded), null, null, null
        )

        viewHolder.root.button_groupName.text = groupName
        viewHolder.root.button_groupName.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewHolder.root.button_groupName.setCompoundDrawables(
                getIcon(viewHolder.root.context, expandableGroup.isExpanded), null, null, null
            )
            adapter.onGroupHeaderClicked(groupName, expandableGroup.isExpanded)
        }
    }

    override fun getDragDirs(): Int {
        return ItemTouchHelper.UP or ItemTouchHelper.DOWN;
    }

    override fun getLayout() = R.layout.item_task_group_header

    private fun getIcon(context: Context, isExpanded: Boolean) : Drawable? {
        val drawable = getDrawable(
            context,
            if (isExpanded)
                R.drawable.ic_expanded
            else
                R.drawable.ic_collapsed
        )
        drawable?.setBounds(0, 0, 60, 60)
        return drawable
    }

}
