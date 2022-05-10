package hu.botagergo.todolist.adapter.filter_criterion_list

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemCompositeFilterCriterionBinding
import hu.botagergo.todolist.filter.*
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.log.logd

class CompositeFilterCriterionItem(val compositeFilter: CompositeFilter<Task>, val context: Context) : BindableItem<ItemCompositeFilterCriterionBinding>() {
    override fun bind(viewBinding: ItemCompositeFilterCriterionBinding, position: Int) {
        viewBinding.data = this
        if (compositeFilter is ConjugateFilter) {
            viewBinding.textViewTitle.text = "AND"
        }

        val adapter = FilterCriterionListAdapter(context)
        for (filter in compositeFilter.filters) {
            logd(this, "add filter")
            adapter.addCriterion(filter)
        }
        viewBinding.recyclerViewCriterionList.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerViewCriterionList.adapter = adapter
    }

    override fun getLayout() = R.layout.item_composite_filter_criterion
}