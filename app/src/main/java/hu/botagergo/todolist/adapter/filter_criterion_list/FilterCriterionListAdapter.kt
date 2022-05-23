package hu.botagergo.todolist.adapter.filter_criterion_list

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.TouchCallback
import hu.botagergo.todolist.adapter.task_view_list.TaskViewItem
import hu.botagergo.todolist.config
import hu.botagergo.todolist.filter.CompositeFilter
import hu.botagergo.todolist.filter.Filter
import hu.botagergo.todolist.filter.PropertyFilter
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task
import java.util.*
import kotlin.collections.ArrayList

class FilterCriterionListAdapter(val context: Context): GroupieAdapter() {
    private val section: Section = Section()
    private val filterCriteria: MutableList<Filter<Task>> = ArrayList()

    interface Listener {
        fun onAddSimpleFilterCriterionClicked(compositeFilter: CompositeFilter<Task>)
    }

    var listener: Listener? = null

    init {
        add(section)
        refresh()
    }

    fun addCriterion(criterion: Filter<Task>) {
        filterCriteria.add(criterion)
        addCriterionItem(criterion)
    }

    fun deleteCriterion(criterionItem: FilterCriterionItem<*>) {
        section.remove(criterionItem)
    }

    private fun addCriterionItem(criterion: Filter<Task>) {
        if (criterion is PropertyFilter<*>) {
            section.add(SimpleFilterCriterionItem(criterion as PropertyFilter<Task>, this))
        } else if (criterion is CompositeFilter<Task>) {
            section.add(CompositeFilterCriterionItem(criterion, this))
        }
    }

    fun refresh() {
        section.clear()
        for (criterion in filterCriteria) {
            addCriterionItem(criterion)
        }
    }

    fun onAddSimpleFilterCriterionClicked(filter: CompositeFilter<Task>) {

    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object: TouchCallback() {
            override fun onMove(
                recyclerView: RecyclerView,
                source: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourceIndex = source.bindingAdapterPosition
                val targetIndex = target.bindingAdapterPosition

                if (sourceIndex == -1 || targetIndex == -1) {
                    return false
                }

                val adapter = this@FilterCriterionListAdapter

                val filterCriterion = adapter.filterCriteria.removeAt(sourceIndex)
                adapter.filterCriteria.add(targetIndex, filterCriterion)

                val groups = adapter.section.groups
                val item = groups.removeAt(sourceIndex)
                groups.add(targetIndex, item)
                adapter.section.update(groups)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                throw NotImplementedError()
            }
        })
    }
}