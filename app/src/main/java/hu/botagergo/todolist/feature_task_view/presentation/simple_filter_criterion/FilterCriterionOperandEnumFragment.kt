package hu.botagergo.todolist.feature_task_view.presentation.simple_filter_criterion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.FragmentFilterCriterionOperandEnumBinding
import hu.botagergo.todolist.feature_task.data.model.TaskEntity
import hu.botagergo.todolist.feature_task.presentation.SimpleSelectItemDialog
import hu.botagergo.todolist.core.util.EnumProperty

class FilterCriterionOperandEnumFragment : Fragment() {

    lateinit var binding: FragmentFilterCriterionOperandEnumBinding

    val viewModel: SimpleFilterCriterionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFilterCriterionOperandEnumBinding.inflate(inflater, container, false)
        binding.editTextValue.setOnClickListener {
            val property = viewModel.property.value as EnumProperty<TaskEntity>
            SimpleSelectItemDialog(
                getString(R.string.sort_by),
                property.values(),
                requireContext()) {
                    viewModel.operand.value = it
                    this.binding.editTextValue.setText(it.value)
            }.show()
        }

        val property = viewModel.property.value as EnumProperty<TaskEntity>
        binding.editTextValue.hint = requireContext().getString(property.name)
        binding.editTextValue.setText(viewModel.operand.value as? String)

        return binding.root
    }

}