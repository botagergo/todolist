package hu.botagergo.todolist.feature_task_view.presentation.simple_filter_criterion

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import hu.botagergo.todolist.*
import hu.botagergo.todolist.databinding.ActivitySimpleFilterCriterionBinding
import hu.botagergo.todolist.feature_task_view.data.filter.predicate.Predicate
import hu.botagergo.todolist.core.util.BooleanProperty
import hu.botagergo.todolist.feature_task_view.data.filter.predicate.PredicateKind
import hu.botagergo.todolist.feature_task.presentation.SimpleSelectItemDialog
import hu.botagergo.todolist.core.util.EnumProperty
import hu.botagergo.todolist.core.util.TextProperty
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FilterCriterionActivity : AppCompatActivity() {

    @Inject lateinit var taskViewRepo: TaskViewRepository

    private val filterUuid by lazy { intent.extras?.getSerializable(EXTRA_UUID) as? UUID }
    val parentFilterUuid by lazy { intent.extras?.getSerializable(EXTRA_PARENT_UUID) as? UUID }
    val taskViewUuid by lazy { intent.extras?.getSerializable(EXTRA_TASK_VIEW_UUID) as? UUID }

    val viewModel: SimpleFilterCriterionViewModel by viewModels {
        SimpleFilterCriterionViewModelFactory(application, taskViewRepo, filterUuid, taskViewUuid, parentFilterUuid)
    }

    val binding: ActivitySimpleFilterCriterionBinding by lazy {
        ActivitySimpleFilterCriterionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.toolbar.setTitle(R.string.edit_filter)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        refreshOperandFragment()
    }

    fun onPropertyClick(view: android.view.View) {
        SimpleSelectItemDialog(
            this.getString(R.string.task_property),
            Predefined.TaskProperty.list,
            this
        ).apply {
            this.setOnDismissListener {
                if (this.selectedItem != null && this.selectedItem != viewModel.property.value) {
                    viewModel.property.value = this.selectedItem
                    viewModel.predicate.value = null
                    refreshOperandFragment()
                }
            }
            this.show()
        }
    }

    fun onOperatorClick(view: android.view.View) {
        val operators = viewModel.property.value?.supportedPredicates() ?: return
        SimpleSelectItemDialog(
            this.getString(R.string.operator),
            operators,
            this
        ).apply {
            this.setOnDismissListener {
                val item = this.selectedItem
                if (item != null) {
                    viewModel.predicate.value = Predicate.create(item)
                    refreshOperandFragment()
                }
            }
            this.show()
        }
    }

    override fun onBackPressed() {
        viewModel.save()
        super.onBackPressed()
    }

    var currentOperandFragment: Fragment? = null

    private fun refreshOperandFragment() {
        if (viewModel.property.value == null || viewModel.predicate.value == null) {
            val fragment = currentOperandFragment
            if (fragment != null) {
                supportFragmentManager.commit {
                    remove(fragment)
                }
            }
            binding.textViewOperand.visibility = View.GONE
            viewModel.operand.value = null
            return
        } else {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                currentOperandFragment = getOperandFragment()
                replace(R.id.fragment_container_view, currentOperandFragment!!)
            }
            binding.textViewOperand.visibility = View.VISIBLE
        }
    }

    private fun getOperandFragment(): Fragment {
        return if (viewModel.property.value is BooleanProperty && viewModel.predicate.value?.kind == PredicateKind.EQUAL) {
            FilterCriterionOperandBooleanFragment()
        } else if (viewModel.property.value is TextProperty && viewModel.predicate.value?.kind == PredicateKind.EQUAL) {
            FilterCriterionOperandStringFragment()
        } else if (viewModel.property.value is EnumProperty && viewModel.predicate.value?.kind == PredicateKind.EQUAL) {
            FilterCriterionOperandEnumFragment()
        } else {
            throw java.lang.IllegalArgumentException()
        }
    }

}