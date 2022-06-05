package hu.botagergo.todolist.feature_task_view.presentation.task_view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import hu.botagergo.todolist.*
import hu.botagergo.todolist.feature_task_view.presentation.task_view.adapter.FilterCriterionListAdapter
import hu.botagergo.todolist.feature_task_view.presentation.task_view.adapter.SortCriterionListAdapter
import hu.botagergo.todolist.databinding.ActivityTaskViewBinding
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task_view.data.sorter.*
import hu.botagergo.todolist.feature_task.presentation.SimpleSelectItemDialog
import hu.botagergo.todolist.feature_task_view.presentation.simple_filter_criterion.FilterCriterionActivity
import hu.botagergo.todolist.core.util.Property
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TaskViewActivity : AppCompatActivity() {

    @Inject lateinit var taskViewRepo: TaskViewRepository

    val binding: ActivityTaskViewBinding by lazy {
        ActivityTaskViewBinding.inflate(layoutInflater)
    }

    val viewModel: TaskViewViewModel by viewModels {
        TaskViewViewModelFactory(taskViewRepo, intent.extras?.getSerializable(EXTRA_UUID) as? UUID)
    }


    private lateinit var sortAdapter: SortCriterionListAdapter
    private lateinit var filterAdapter: FilterCriterionListAdapter

    private lateinit var availableSortSubjects: Array<Property<TaskEntity>>

    private val isEdit: Boolean by lazy {
        intent.extras?.getSerializable(EXTRA_IS_EDIT) as Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.toolbar.setTitle(R.string.task_view)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        if (!isEdit) {
            binding.toolbar.inflateMenu(R.menu.menu_task_view_activity)
            binding.toolbar.setOnMenuItemClickListener { onMenuItemClicked(it) }
        }

        binding.imageButtonSelectName.setOnClickListener { onButtonSelectNameClicked() }

        val sorter = viewModel.sorter.value
        sortAdapter = if (sorter is CompositeSorter<TaskEntity>) {
            binding.checkBoxManualOrder.isChecked = false
            val sortCriteria = sorter.sortCriteria.map { it as PropertySortCriterion<TaskEntity> }.toMutableList()
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
        } else {
            binding.buttonCreateCriterion.visibility = View.VISIBLE
        }

        binding.buttonCreateCriterion.setOnClickListener {
            startActivity(
                Intent(this, FilterCriterionActivity::class.java).apply {
                    putExtra(EXTRA_TASK_VIEW_UUID, viewModel.uuid)
                }
            )
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

    private fun saveTaskView() {
        viewModel.sorter.value =
            if (binding.checkBoxManualOrder.isChecked)
                ManualTaskSorter()
            else
                CompositeSorter(sortAdapter.sortCriteria.toMutableList())

        taskViewRepo.insert(viewModel.taskView)
    }

    override fun onBackPressed() {
        if (isEdit) {
            saveTaskView()
        }
        super.onBackPressed()
    }

    private fun onMenuItemClicked(menuItem: MenuItem): Boolean {
        return if (menuItem.itemId == R.id.menu_item_save) {
            saveTaskView()
            onBackPressed()
            true
        } else {
            false
        }
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