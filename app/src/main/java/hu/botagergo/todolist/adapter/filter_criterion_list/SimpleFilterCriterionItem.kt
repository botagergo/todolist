package hu.botagergo.todolist.adapter.filter_criterion_list

import android.content.Context
import android.content.Intent
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.EXTRA_UUID
import hu.botagergo.todolist.R
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.ItemSimpleFilterCriterionBinding
import hu.botagergo.todolist.filter.PropertyFilter
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.util.NamedByResource
import hu.botagergo.todolist.view.EditFilterCriterionActivity

class SimpleFilterCriterionItem(val taskPropertyFilter: PropertyFilter<Task>, val context: Context) : BindableItem<ItemSimpleFilterCriterionBinding>() {
    override fun bind(viewBinding: ItemSimpleFilterCriterionBinding, position: Int) {
        viewBinding.data = this
    }

    fun onClicked() {
        config
        val intent = Intent(context, EditFilterCriterionActivity::class.java)
        intent.putExtra(EXTRA_UUID, taskPropertyFilter.uuid)
        context.startActivity(intent)
    }

    val operandValue: String?
        get() {
            val operand = taskPropertyFilter.operand
            return if (operand is NamedByResource) {
                context.getString(operand.name)
            } else {
                operand?.toString()
            }
        }

    override fun getLayout() = R.layout.item_simple_filter_criterion
}