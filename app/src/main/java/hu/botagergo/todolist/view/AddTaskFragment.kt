package hu.botagergo.todolist.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.FragmentAddTaskBinding
import hu.botagergo.todolist.view_model.TaskListViewModel
import hu.botagergo.todolist.view_model.TaskViewModel
import hu.botagergo.todolist.view_model.TaskViewModelFactory
import java.time.LocalDate
import java.time.LocalTime

class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(requireActivity().application, 0)
    }

    private val taskListViewModel: TaskListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.handlers = this
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_task_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ((item.itemId == android.R.id.home) or (item.itemId == R.id.menu_item_cancel)) {
            findNavController().popBackStack()
        } else if (item.itemId == R.id.menu_item_done) {
            taskListViewModel.addTask(viewModel.task)
            findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onStatusClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        val dialog = SimpleSelectItemDialog(
            getString(R.string.status),
            Predefined.TaskProperty.status.values(),
            requireContext()
        )
        dialog.setOnDismissListener {
            val item = dialog.selectedItem
            if (item != null) {
                viewModel.status.value = item
            }
        }
        dialog.show()
    }

    fun onCancelStatusClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        viewModel.status.value = null
    }

    fun onContextClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        val dialog = SimpleSelectItemDialog(
            getString(R.string.context),
            Predefined.TaskProperty.context.values(),
            requireContext()
        )
        dialog.setOnDismissListener {
            val item = dialog.selectedItem
            if (item != null) {
                viewModel.context.value = item
            }
        }
        dialog.show()
    }

    fun onCancelContextClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        viewModel.context.value = null
    }

    fun onStartDateClicked(view: View) {
        showDatePickerDialog(view, true)
    }

    fun onStartTimeClicked(view: View) {
        showTimePickerDialog(view, true)
    }

    fun onDueDateClicked(view: View) {
        showDatePickerDialog(view, false)
    }

    fun onDueTimeClicked(view: View) {
        showTimePickerDialog(view, false)
    }

    private fun showDatePickerDialog(view: View, start: Boolean) {
        val dateNow = LocalDate.now()
        val dialog = DatePickerDialog(
            view.context, R.style.DateTimePickerDialogTheme,
            { _, p1, p2, p3 ->
                val selectedDate = LocalDate.of(
                    p1,
                    p2 + 1,
                    p3
                ) // In the dialog month numbering starts from 0 but 1 in LocalDate
                if (start) {
                    viewModel.startDate.value = selectedDate
                } else {
                    viewModel.dueDate.value = selectedDate
                }
            },
            dateNow.year, dateNow.monthValue-1, dateNow.dayOfMonth)
        dialog.show()
    }

    private fun showTimePickerDialog(view: View, start: Boolean) {
        val timeNow = LocalTime.now()
        val dialog = TimePickerDialog(
            view.context, R.style.DateTimePickerDialogTheme,
            { _, p1, p2 ->
                val selectedTime = LocalTime.of(p1, p2)
                if (start) {
                    viewModel.startTime.value = selectedTime
                } else {
                    viewModel.dueTime.value = selectedTime
                }
            },
            timeNow.hour, timeNow.minute, true)
        dialog.show()
    }

    fun onCancelDateClicked(view: View) {
        when (view.id) {
            R.id.imageButton_cancelStartDate -> {
                viewModel.startDate.value = null
            }
            R.id.imageButton_cancelStartTime -> {
                viewModel.startTime.value = null
            }
            R.id.imageButton_cancelDueDate -> {
                viewModel.dueDate.value = null
            }
            R.id.imageButton_cancelDueTime -> {
                viewModel.dueTime.value = null
            }
        }
    }

}