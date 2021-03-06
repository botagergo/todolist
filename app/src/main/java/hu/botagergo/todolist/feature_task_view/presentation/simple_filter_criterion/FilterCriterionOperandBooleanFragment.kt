package hu.botagergo.todolist.feature_task_view.presentation.simple_filter_criterion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.FragmentFilterCriterionOperandBooleanBinding

class FilterCriterionOperandBooleanFragment : Fragment() {

    lateinit var binding: FragmentFilterCriterionOperandBooleanBinding

    val viewModel: SimpleFilterCriterionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFilterCriterionOperandBooleanBinding.inflate(inflater, container, false)
        binding.switchEquals.setOnCheckedChangeListener { _, checked ->
            onCheckedChange(checked)
        }

        binding.switchEquals.isChecked = viewModel.operand.value as? Boolean ?: false
        onCheckedChange(binding.switchEquals.isChecked)

        return binding.root
    }

    private fun onCheckedChange(checked: Boolean) {
        binding.textViewEquals.text = this.getString(if (checked) R.string.true_ else R.string.false_)
        viewModel.operand.value = checked
    }

}