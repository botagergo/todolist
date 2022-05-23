package hu.botagergo.todolist.adapter.filter_criterion_list

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.EXTRA_UUID
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemCompositeFilterCriterionBinding
import hu.botagergo.todolist.databinding.ItemSimpleFilterCriterionBinding
import hu.botagergo.todolist.filter.*
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.log.logd
import hu.botagergo.todolist.view.FilterCriterionActivity

class CompositeFilterCriterionItem(private val compositeFilter: CompositeFilter<Task>, adapter: FilterCriterionListAdapter) :
    FilterCriterionItem<ItemCompositeFilterCriterionBinding>(adapter) {

    override fun bind(viewBinding: ItemCompositeFilterCriterionBinding, position: Int) {
        viewBinding.data = this
        if (compositeFilter is ConjugateFilter) {
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