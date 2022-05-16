package hu.botagergo.todolist.view

import hu.botagergo.todolist.log.logd
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.ActivityEditTaskViewBinding
import hu.botagergo.todolist.databinding.FragmentEditTaskBinding
import hu.botagergo.todolist.databinding.FragmentFilterCriterionOperandStringBinding
import hu.botagergo.todolist.view_model.SimpleFilterCriterionViewModel

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