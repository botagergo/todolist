package hu.botagergo.todolist.feature_task.presentation.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.botagergo.todolist.EXTRA_IS_EDIT
import hu.botagergo.todolist.Predefined
import hu.botagergo.todolist.R
import hu.botagergo.todolist.core.util.EnumValue
import hu.botagergo.todolist.feature_task.presentation.SimpleSelectItemDialog
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class TaskFragment : Fragment() {

    private val isEdit: Boolean by lazy {
        requireArguments().getBoolean(EXTRA_IS_EDIT)
    }

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!isEdit) {
            setHasOptionsMenu(true)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TaskScreen(viewModel)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
            R.id.menu_item_save -> {
                lifecycleScope.launch {
                    viewModel.save()
                }
                findNavController().popBackStack()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onStop() {
        if (isEdit) {
            lifecycleScope.launch {
                viewModel.save()
            }
        }
        super.onStop()
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        OutlinedTextField(
            value = viewModel.title.value,
            label = {
                Text(stringResource(R.string.title))
            },
            onValueChange = {
                viewModel.title.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 26.sp, color = Color.Black),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = viewModel.comments.value,
            label = {
                Text(stringResource(R.string.comments))
            },
            onValueChange = {
                viewModel.comments.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        CancelableEnumTextField(
            value = viewModel.status,
            label = R.string.status,
            toResourceId = { status: EnumValue ->
                status.value
            },
            onClick = {
                SimpleSelectItemDialog(
                    R.string.status,
                    Predefined.TaskProperty.status.values(),
                    context
                ) {
                    viewModel.status.value = it
                }.show()
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        CancelableEnumTextField(
            value = viewModel.context,
            label = R.string.context,
            toResourceId = { context: EnumValue ->
                context.value
            },
            onClick = {
                SimpleSelectItemDialog(
                    R.string.context,
                    Predefined.TaskProperty.context.values(),
                    context
                ) {
                    viewModel.context.value = it
                }.show()
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        CancelableEnumTextField(
            value = viewModel.startDate,
            label = R.string.start_date,
            toString = {
                DateTimeFormatter.ofLocalizedDate(
                    FormatStyle.FULL
                ).format(it)
            },
            onClick = {
                val dateNow = LocalDate.now()
                DatePickerDialog(
                    context, R.style.DateTimePickerDialogTheme,
                    { _, p1, p2, p3 ->
                        viewModel.startDate.value = LocalDate.of(p1, p2 + 1, p3)
                    },
                    dateNow.year, dateNow.monthValue - 1, dateNow.dayOfMonth
                ).show()
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        CancelableEnumTextField(
            value = viewModel.startTime,
            label = R.string.start_time,
            toString = {
                DateTimeFormatter
                    .ofLocalizedTime(FormatStyle.FULL)
                    .withZone(ZoneId.systemDefault()).format(it)
            },
            onClick = {
                val timeNow = LocalTime.now()
                TimePickerDialog(
                    context, R.style.DateTimePickerDialogTheme,
                    { _, p1, p2 ->
                        viewModel.startTime.value = LocalTime.of(p1, p2)
                    },
                    timeNow.hour, timeNow.minute, true
                ).show()
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        CancelableEnumTextField(
            value = viewModel.dueDate,
            label = R.string.due_date,
            toString = {
                DateTimeFormatter.ofLocalizedDate(
                    FormatStyle.FULL
                ).format(it)
            },
            onClick = {
                val dateNow = LocalDate.now()
                DatePickerDialog(
                    context, R.style.DateTimePickerDialogTheme,
                    { _, p1, p2, p3 ->
                        viewModel.dueDate.value = LocalDate.of(p1, p2 + 1, p3)
                    },
                    dateNow.year, dateNow.monthValue - 1, dateNow.dayOfMonth
                ).show()
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        CancelableEnumTextField(
            value = viewModel.dueTime,
            label = R.string.due_time,
            toString = {
                DateTimeFormatter
                    .ofLocalizedTime(FormatStyle.FULL)
                    .withZone(ZoneId.systemDefault()).format(it)
            },
            onClick = {
                val timeNow = LocalTime.now()
                TimePickerDialog(
                    context, R.style.DateTimePickerDialogTheme,
                    { _, p1, p2 ->
                        viewModel.dueTime.value = LocalTime.of(p1, p2)
                    },
                    timeNow.hour, timeNow.minute, true
                ).show()
            },
        )
    }
}

@Composable
fun <T> CancelableEnumTextField(
    value: MutableState<T?>,
    label: Int,
    onClick: () -> Unit,
    toString: ((T) -> String)? = null,
    toResourceId: ((T) -> Int)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = if (value.value != null) {
                if (toString != null)
                    toString(value.value!!)
                else if (toResourceId != null)
                    stringResource(toResourceId(value.value!!))
                else
                    value.value!!.toString()
            } else {
                ""
            },
            label = {
                Text(stringResource(label))
            },
            textStyle = TextStyle(fontSize = 22.sp, color = Color.Black),
            onValueChange = {},
            enabled = false,

            modifier = Modifier
                .clickable {
                    onClick()
                }
                .weight(1.0f),
            singleLine = true
        )
        if (value.value != null) {
            IconButton(onClick = {
                value.value = null
            }) {
                Icon(
                    Icons.Filled.Clear,
                    tint = Color.Red,
                    contentDescription = "Remove"
                )
            }
        }
    }
}