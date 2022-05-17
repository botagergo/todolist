package hu.botagergo.todolist.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.botagergo.todolist.EXTRA_UUID
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.R
import hu.botagergo.todolist.adapter.filter_criterion_list.FilterCriterionListAdapter
import hu.botagergo.todolist.adapter.sort_criterion_list.SortCriterionListAdapter
import hu.botagergo.todolist.config
import hu.botagergo.todolist.databinding.ActivityEditTaskViewBinding
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.sorter.*
import hu.botagergo.todolist.util.Property
import hu.botagergo.todolist.view_model.TaskViewViewModel
import hu.botagergo.todolist.view_model.TaskViewViewModelFactory
import java.util.*

class EditTaskViewActivity : AppCompatActivity() {
    val binding: ActivityEditTaskViewBinding by lazy {
        ActivityEditTaskViewBinding.inflate(layoutInflater)
    }

    val viewModel: TaskViewViewModel by viewModels {
        TaskViewViewModelFactory(application, intent.extras?.getSerializable(EXTRA_UUID) as? UUID)
    }

    private lateinit var sortAdapter: SortCriterionListAdapter
    private lateinit var filterAdapter: FilterCriterionListAdapter

    private lateinit var availableSortSubjects: Array<Property<Task>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.toolbar.setTitle(R.string.task_view)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        binding.imageButtonSelectName.setOnClickListener { onButtonSelectNameClicked() }

        val sorter = viewModel.sorter.value
        sortAdapter = if (sorter is CompositeSorter<Task>) {
            binding.checkBoxManualOrder.isChecked = false
            val sortCriteria = sorter.sortCriteria.map { it as PropertySortCriterion<Task> }.toMutableList()
            SortCriterionListAdapter(sortCriteria, this)
        } else {
            binding.checkBoxManualOrder.isChecked = true
            SortCriterionListAdapter(mutableListOf(), this)
        }
        sortAdapter.listener = object : SortCriterionListAdapter.Listener {
            override fun onSortSubjectListChanged() {
                updateAvailableSortSubjects()
            }
        }

        sortAdapter.getItemTouchHelper().attachToRecyclerView(binding.recyclerViewSortCriteria)

        binding.recyclerViewSortCriteria.itemAnimator = null

        updateAvailableSortSubjects()

        onCheckBoxManualOrderChanged(binding.checkBoxManualOrder.isChecked)

        binding.recyclerViewSortCriteria.adapter = sortAdapter
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

        filterAdapter = FilterCriterionListAdapter(this)
        binding.recyclerViewFilterCriteria.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFilterCriteria.adapter = filterAdapter

        if (viewModel.filter.value != null) {
            filterAdapter.addCriterion(viewModel.filter.value!!)
        }
    }

    override fun onResume() {
        filterAdapter.refresh()
        super.onResume()
    }

    private fun onButtonSelectNameClicked() {
        binding.editTextName.requestFocus()
        binding.editTextName.selectAll()

        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editTextName, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun onCheckBoxManualOrderChanged(checked: Boolean) {
        val visibility = if (checked) View.GONE else View.VISIBLE
        binding.recyclerViewSortCriteria.visibility = visibility
        updateAddSortSubjectButtonVisiblity()
    }

    private fun onButtonAddSortCriterionClicked() {
        SimpleSelectItemDialog(
            getString(R.string.sort_by),
            availableSortSubjects,
            this).apply {
            this.setOnDismissListener {
                if (this.selectedItem != null) {
                    val sortSubject = this.selectedItem!!
                    sortAdapter.addCriterion(PropertySortCriterion(sortSubject))
                    updateAvailableSortSubjects()
                }
            }
            this.show()
        }

    }

    override fun onBackPressed() {
        viewModel.sorter.value =
            if (binding.checkBoxManualOrder.isChecked)
                ManualTaskSorter()
            else
                CompositeSorter(sortAdapter.sortCriteria.toMutableList())

        config.taskViews.put(viewModel.taskView)
        super.onBackPressed()
    }

    private fun updateAddSortSubjectButtonVisiblity() {
        val visible = !(availableSortSubjects.isEmpty() || binding.checkBoxManualOrder.isChecked)
        binding.buttonAddCriterion.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun updateAvailableSortSubjects() {
        availableSortSubjects = Predefined.TaskProperty.list.filter { property ->
            property.comparable && sortAdapter.sortCriteria.find { criterion ->
                criterion.property == property
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
                binding.buttonGroup.setText(item.name)
            }
        }
        dialog.show()
    }

    private fun onButtonCancelGrouperClicked() {
        viewModel.grouper.value = null
        binding.buttonGroup.setText("")
    }

}