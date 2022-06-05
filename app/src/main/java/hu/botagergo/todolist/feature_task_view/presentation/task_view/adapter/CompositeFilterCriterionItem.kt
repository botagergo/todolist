package hu.botagergo.todolist.feature_task_view.presentation.task_view.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemCompositeFilterCriterionBinding
import hu.botagergo.todolist.feature_task_view.domain.model.filter.*
import hu.botagergo.todolist.feature_task.data.model.TaskEntity

class CompositeFilterCriterionItem(private val compositeFilter: CompositeFilter<TaskEntity>, adapter: FilterCriterionListAdapter) :
    FilterCriterionItem<ItemCompositeFilterCriterionBinding>(adapter) {

    override fun bind(viewBinding: ItemCompositeFilterCriterionBinding, position: Int) {
        viewBinding.data = this
        if (compositeFilter is AndFilter) {
            viewBinding.textViewTitle.text = "AND"
        }

        val adapter = FilterCriterionListAdapter(adapter.context)
        for (filter in compositeFilter.filters) {
            adapter.addCriterion(filter)
        }
        viewBinding.recyclerViewCriterionList.layoutManager = LinearLayoutManager(adapter.context)
        viewBinding.recyclerViewCriterionList.adapter = adapter
        adapter.getItemTouchHelper().attachToRecyclerView(viewBinding.recyclerViewCriterionList)

        viewBinding.imageButtonAdd.setOnClickListener {
            adapter.onAddSimpleFilterCriterionClicked(compositeFilter)
        }

        viewBinding.cardView.setOnLongClickListener {
            showPopup(viewBinding.cardView)
            true
        }
    }

    override fun getLayout() = R.layout.item_composite_filter_criterion
    override fun getDragDirs() = ItemTouchHelper.UP or ItemTouchHelper.DOWN

}