package hu.botagergo.todolist.feature_task_view.presentation.task_view.adapter

import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import hu.botagergo.todolist.EXTRA_UUID
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemSimpleFilterCriterionBinding
import hu.botagergo.todolist.feature_task_view.domain.model.filter.PropertyFilter
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.core.util.NamedByResource
import hu.botagergo.todolist.feature_task_view.presentation.simple_filter_criterion.FilterCriterionActivity

class SimpleFilterCriterionItem(val taskPropertyFilter: PropertyFilter<TaskEntity>, adapter: FilterCriterionListAdapter) :
    FilterCriterionItem<ItemSimpleFilterCriterionBinding>(adapter) {

    override fun bind(viewBinding: ItemSimpleFilterCriterionBinding, position: Int) {
        viewBinding.data = this

        viewBinding.cardView.setOnClickListener {
            adapter.context.startActivity(
                Intent(adapter.context, FilterCriterionActivity::class.java).apply {
                    putExtra(EXTRA_UUID, taskPropertyFilter.uuid)
                }
            )
        }

        viewBinding.cardView.setOnLongClickListener {
            showPopup(viewBinding.cardView)
            true
        }
    }

    val operandValue: String?
        get() {
            val operand = taskPropertyFilter.operand
            return if (operand is NamedByResource) {
                adapter.context.getString(operand.name)
            } else {
                operand?.toString()
            }
        }

    override fun getLayout() = R.layout.item_simple_filter_criterion
    override fun getDragDirs() = ItemTouchHelper.UP or ItemTouchHelper.DOWN

}