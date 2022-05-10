package hu.botagergo.todolist.adapter.filter_criterion_list

import android.content.Context
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import hu.botagergo.todolist.filter.CompositeFilter
import hu.botagergo.todolist.filter.Filter
import hu.botagergo.todolist.filter.PropertyFilter
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.model.Task

class FilterCriterionListAdapter(val context: Context): GroupieAdapter() {
    private val section: Section = Section()
    private val filterCriteria: MutableList<Filter<Task>> = ArrayList()

    init {
        add(section)
        refresh()
    }

    fun addCriterion(criterion: Filter<Task>) {
        filterCriteria.add(criterion)
        addCriterionItem(criterion)
    }

    private fun addCriterionItem(criterion: Filter<Task>) {
        if (criterion is PropertyFilter<*>) {
            logd(this, "criterion is property")
            section.add(SimpleFilterCriterionItem(criterion as PropertyFilter<Task>, context))
        } else if (criterion is CompositeFilter<Task>) {
            logd(this, "criterion is composite")
            section.add(CompositeFilterCriterionItem(criterion, context))
        }
    }

    fun refresh() {
        section.clear()
        for (criterion in filterCriteria) {
            addCriterionItem(criterion)
        }
    }
}