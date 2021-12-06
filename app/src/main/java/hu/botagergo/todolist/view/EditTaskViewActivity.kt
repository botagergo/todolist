package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.R
import hu.botagergo.todolist.adapter.sort_criterion_list.SortCriterionListAdapter
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.ActivityEditTaskViewBinding
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.SimpleSorter
import hu.botagergo.todolist.sorter.SortCriterion
import hu.botagergo.todolist.sorter.SortSubject
import hu.botagergo.todolist.sorter.TaskReorderableSorter
import hu.botagergo.todolist.view_model.TaskViewViewModel
import hu.botagergo.todolist.view_model.TaskViewViewModelFactory
import java.util.*

class EditTaskViewActivity : AppCompatActivity() {
    val binding: ActivityEditTaskViewBinding by lazy {
        ActivityEditTaskViewBinding.inflate(layoutInflater)
    }

    val viewModel: TaskViewViewModel by viewModels {
        TaskViewViewModelFactory(application, intent.extras!!.getSerializable("uuid") as UUID)
    }

    lateinit var adapter: SortCriterionListAdapter

    private lateinit var availableSortSubjects: Array<SortSubject<Task>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.toolbar.setTitle(R.string.edit_task_view)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbar.inflateMenu(R.menu.menu_edit_task_view_activity)
        binding.toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }

        val sorter = viewModel.sorter.value
        adapter = if (sorter is SimpleSorter<*>) {
            binding.checkBoxManualOrder.isChecked = false
            val sortCriteria = sorter.sortCriteria as MutableList<SortCriterion<Task>>
            SortCriterionListAdapter(sortCriteria, this)
        } else {
            binding.checkBoxManualOrder.isChecked = true
            SortCriterionListAdapter(mutableListOf(), this)
        }
        adapter.listener = object : SortCriterionListAdapter.Listener {
            override fun onSortSubjectListChanged() {
                updateAvailableSortSubjects()
            }
        }

        adapter.getItemTouchHelper().attachToRecyclerView(binding.recyclerViewSortCriteria)

        binding.recyclerViewSortCriteria.itemAnimator = null

        updateAvailableSortSubjects()

        onCheckBoxManualOrderChanged(binding.checkBoxManualOrder.isChecked)

        binding.recyclerViewSortCriteria.adapter = adapter
        binding.recyclerViewSortCriteria.layoutManager = LinearLayoutManager(this)
        binding.buttonAddCriterion.setOnClickListener {
            onButtonAddSortCriterionClicked()
        }

        binding.checkBoxManualOrder.setOnCheckedChangeListener { _, checked ->
            onCheckBoxManualOrderChanged(checked)
        }

        binding.buttonGroup.setText(viewModel.grouper.value?.toString(this))
        binding.buttonGroup.setOnClickListener { onButtonGrouperClicked(it) }
        binding.imageButtonCancelGroup.setOnClickListener { onButtonCancelGrouperClicked() }
    }

    private fun onCheckBoxManualOrderChanged(checked: Boolean) {
        val visibility = if (checked) View.GONE else View.VISIBLE
        binding.recyclerViewSortCriteria.visibility = visibility
        updateAddSortSubjectButtonVisiblity()
    }

    private fun onButtonAddSortCriterionClicked() {
        val dialog =
            SimpleSelectItemDialog(getString(R.string.sort_by), availableSortSubjects, this)
        dialog.setOnDismissListener {
            if (dialog.selectedItem != null) {
                val sortSubject = dialog.selectedItem!!
                val criterion = sortSubject.makeCriterion(SortCriterion.Order.ASCENDING)
                adapter.addCriterion(criterion)
                updateAvailableSortSubjects()
            }
        }
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_done -> {
                viewModel.sorter.value =
                    if (binding.checkBoxManualOrder.isChecked)
                        TaskReorderableSorter()
                    else
                        SimpleSorter(adapter.sortCriteria)

                config.taskViews.put(viewModel.taskView)
                onBackPressed()
                true
            }
            R.id.menu_item_cancel -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun updateAddSortSubjectButtonVisiblity() {
        val visible = !(availableSortSubjects.isEmpty() || binding.checkBoxManualOrder.isChecked)
        binding.buttonAddCriterion.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun updateAvailableSortSubjects() {
        availableSortSubjects = Predefined.SortSubjects.list.filter { subject ->
            adapter.sortCriteria.find { criterion ->
                criterion.getSubject() == subject
            } == null
        }.toTypedArray()
        updateAddSortSubjectButtonVisiblity()
    }

    private fun onButtonGrouperClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        val dialog = SimpleSelectItemDialog(R.string.group_by, Predefined.GroupBy.list, this)
        dialog.setOnDismissListener {
            val item = dialog.selectedItem
            if (item != null) {
                viewModel.grouper.value = item
                binding.buttonGroup.setText(item.getName())
            }
        }
        dialog.show()
    }

    private fun onButtonCancelGrouperClicked() {
        viewModel.grouper.value = null
        binding.buttonGroup.setText("")
    }

}