package hu.botagergo.todolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.FragmentFilterCriterionOperandEnumBinding
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.util.EnumProperty
import hu.botagergo.todolist.view_model.SimpleFilterCriterionViewModel

class FilterCriterionOperandEnumFragment : Fragment() {

    lateinit var binding: FragmentFilterCriterionOperandEnumBinding

    val viewModel: SimpleFilterCriterionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFilterCriterionOperandEnumBinding.inflate(inflater, container, false)
        binding.editTextValue.setOnClickListener {
            val property = viewModel.property.value as EnumProperty<Task>
            SimpleSelectItemDialog(
                getString(R.string.sort_by),
                property.values(),
                requireContext()).apply {
                this.setOnDismissListener {
                    val selectedItem = this.selectedItem
                    if (selectedItem != null) {
                        viewModel.operand.value = this.selectedItem
                        this@FilterCriterionOperandEnumFragment.binding.editTextValue.setText(selectedItem.value)
                    }
                }
                this.show()
            }
        }

        val property = viewModel.property.value as EnumProperty<Task>
        binding.editTextValue.hint = requireContext().getString(property.name)
        binding.editTextValue.setText(viewModel.operand.value as? String)

        return binding.root
    }

}