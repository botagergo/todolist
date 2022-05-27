package hu.botagergo.todolist.feature_task_view.presentation.simple_filter_criterion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import hu.botagergo.todolist.databinding.FragmentFilterCriterionOperandStringBinding

class FilterCriterionOperandStringFragment : Fragment() {

    lateinit var binding: FragmentFilterCriterionOperandStringBinding

    val viewModel: SimpleFilterCriterionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterCriterionOperandStringBinding.inflate(inflater, container, false)
        binding.editTextString.doOnTextChanged { text, _, _, _ ->
            viewModel.operand.value = text.toString()
        }
        binding.editTextString.setText(viewModel.operand.value as? String)
        return binding.root
    }

}