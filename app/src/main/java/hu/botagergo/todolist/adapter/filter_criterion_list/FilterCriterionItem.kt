package hu.botagergo.todolist.adapter.filter_criterion_list

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.databinding.ViewDataBinding
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemSimpleFilterCriterionBinding

abstract class FilterCriterionItem<T: ViewDataBinding>(val adapter: FilterCriterionListAdapter) : BindableItem<T>() {
    protected fun showPopup(view: View) {
        val popupMenu = PopupMenu(adapter.context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_filter_criterion_item, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.menu_item_delete) {
                    adapter.deleteCriterion(this)
            }
            true
        }
        popupMenu.show()
    }

}