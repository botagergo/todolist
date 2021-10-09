package hu.botagergo.todolist.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import hu.botagergo.todolist.R
import hu.botagergo.todolist.databinding.FragmentAddTaskBinding
import hu.botagergo.todolist.model.Task
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spinnerStatus.adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, Task.Status.values()
        )
        binding.spinnerContext.adapter = ArrayAdapter(
            view.context, android.R.layout.simple_spinner_dropdown_item, Task.Context.values()
        )
        binding.lifecycleOwner = this
        super.onViewCreated(view, savedInstanceState)
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
                val selectedDate = LocalDate.of(p1, p2, p3)
                if (start) {
                    viewModel.startDate.value = selectedDate.plusMonths(1)
                } else {
                    viewModel.dueDate.value = selectedDate.plusMonths(1)
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