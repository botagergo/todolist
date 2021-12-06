package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class TaskViewViewModelFactory(val application: Application, private val uuid: UUID) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, UUID::class.java)
            .newInstance(application, uuid)
    }
}