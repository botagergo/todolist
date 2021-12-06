package hu.botagergo.todolist.adapter.sort_criterion_list

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.TouchCallback
import com.xwray.groupie.databinding.BindableItem
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ItemSortCriterionBinding
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.SortCriterion
import hu.botagergo.todolist.view.SimpleSelectItemDialog
import java.util.*

class SortCriterionListAdapter(
    var sortCriteria: MutableList<SortCriterion<Task>>,
    val context: Context
) : GroupieAdapter() {

    interface Listener {
        fun onSortSubjectListChanged()
    }

    var listener: Listener? = null

    val section: Section = Section()

    init {
        add(section)
        refresh()
    }

    fun addCriterion(criterion: SortCriterion<Task>) {
        section.add(SortCriterionItem(criterion))
        sortCriteria.add(criterion)
    }

    private fun refresh() {
        section.clear()
        for (criterion in sortCriteria) {
            section.add(SortCriterionItem(criterion))
        }
    }

    inner class SortCriterionItem(private val sortCriterion: SortCriterion<Task>) :
        BindableItem<ItemSortCriterionBinding>() {

        val adapter = this@SortCriterionListAdapter

        override fun bind(viewBinding: ItemSortCriterionBinding, position: Int) {
            viewBinding.buttonSortSubject.text =
                context.getText(sortCriterion.getSubject().getName())
            viewBinding.buttonSortSubject.setOnClickListener {
                val dialog = SimpleSelectItemDialog(
                    context.getString(R.string.sort_by),
                    Predefined.SortSubjects.list,
                    context
                )
                dialog.setOnDismissListener {
                    if (dialog.selectedItem != null) {
                        val prevSortCriterionInd = sortCriteria.indexOfFirst {
                            it.getSubject() == dialog.selectedItem
                        }
                        if (prevSortCriterionInd != -1) {
                            val items = section.groups
                            items.removeAt(prevSortCriterionInd)
                            section.update(items)

                            sortCriteria.removeAt(prevSortCriterionInd)
                        }

                        sortCriterion.setSubject(dialog.selectedItem!!)
                        viewBinding.buttonSortSubject.text =
                            context.getText(sortCriterion.getSubject().getName())

                        listener?.onSortSubjectListChanged()
                    }
                }
                dialog.show()
            }

            viewBinding.buttonSortOrder.text = sortCriterion.getOrder().name
            viewBinding.buttonSortOrder.setOnClickListener {
                val dialog = SimpleSelectItemDialog(
                    context.getString(R.string.sort_order),
                    SortCriterion.Order.values(),
                    context
                )
                dialog.setOnDismissListener {
                    if (dialog.selectedItem != null) {
                        sortCriterion.setOrder(dialog.selectedItem!!)
                        viewBinding.buttonSortOrder.text = sortCriterion.getOrder().name
                    }
                }
                dialog.show()
            }

            viewBinding.imageButtonRemove.setOnClickListener {
                section.remove(this)
                sortCriteria.remove(sortCriterion)
                listener?.onSortSubjectListChanged()
            }

        }

        override fun getLayout(): Int = R.layout.item_sort_criterion
        override fun getDragDirs(): Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN

    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(MyTouchCallback())
    }

    inner class MyTouchCallback : TouchCallback() {
        override fun onMove(
            recyclerView: RecyclerView, source: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val adapter = this@SortCriterionListAdapter

            val sourceItem = adapter.getItem(source.bindingAdapterPosition) as SortCriterionItem
            val targetItem = adapter.getItem(target.bindingAdapterPosition) as SortCriterionItem

            val items = section.groups
            val sourceIndex = items.indexOf(sourceItem)
            val targetIndex = items.indexOf(targetItem)
            items.remove(sourceItem)
            items.add(targetIndex, sourceItem)

            section.update(items)

            Collections.swap(sortCriteria, sourceIndex, targetIndex)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            throw NotImplementedError()
        }
    }

}