package hu.botagergo.todolist.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import hu.botagergo.todolist.EXTRA_UUID
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ActivitySimpleFilterCriterionBinding
import hu.botagergo.todolist.filter.PropertyFilter
import hu.botagergo.todolist.filter.predicate.Predicate
import hu.botagergo.todolist.util.BooleanProperty
import hu.botagergo.todolist.filter.predicate.PredicateKind
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.util.EnumProperty
import hu.botagergo.todolist.util.TextProperty
import hu.botagergo.todolist.view_model.SimpleFilterCriterionViewModel
import hu.botagergo.todolist.view_model.SimpleFilterCriterionViewModelFactory
import java.util.*

class EditFilterCriterionActivity : AppCompatActivity() {

    val uuid: UUID by lazy {
        intent.extras?.getSerializable(EXTRA_UUID) as? UUID ?: throw IllegalArgumentException()
    }

    val viewModel: SimpleFilterCriterionViewModel by viewModels {
        SimpleFilterCriterionViewModelFactory(application, uuid)
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