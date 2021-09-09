package hu.botagergo.todolist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import hu.botagergo.todolist.model.Task
import hu.botagergo.todolist.model.AppDatabase
import hu.botagergo.todolist.model.TaskDao

class TaskListViewModel(application: Application)
    : AndroidViewModel(application) {
    private var _tasksLiveData: MutableLiveData<ArrayList<Task>>
    private var _tasks: ArrayList<Task>
    private var _taskDao: TaskDao

    init {
        val db = Room.databaseBuilder(
            application, AppDatabase::class.java, "task"
        ).allowMainThreadQueries().build()

        _taskDao = db.taskDao()
        _tasks = ArrayList(_taskDao.getAll())
        _tasksLiveData = MutableLiveData(_tasks)
    }

    fun getTasks() : LiveData<ArrayList<Task>> {
        return _tasksLiveData
    }

    fun addTask(task: Task) {
        val uid = _taskDao.insert(task.copy())
        _tasks.add(task.copy(uid = uid))
        _tasksLiveData.value = _tasks
    }

    fun deleteTask(task: Task) {
        _taskDao.delete(task)
        _tasks.remove(task)
        _tasksLiveData.value = _tasks
    }

    fun deleteAll() {
        _taskDao.deleteAll()
        _tasks.clear()
        _tasksLiveData.value = _tasks
    }

    fun updateTask(task: Task) {
        _taskDao.update(task)

        val index = _tasks.indexOfFirst {  it.uid == task.uid }
        _tasks[index] = task

        _tasksLiveData.value = _tasks
    }

    fun addSampleData() {
        addTask(Task("Go for a walk", "If you enjoy (re)watching it once in a while, you might as well do it now, because it’s getting removed from Netflix. Why? Because all Nickelodeon content is being moved to Paramount+, a new streaming service.", Task.Status.NextAction, Task.Context.Work))
        addTask(Task("Take out trash", "Did you know many of the earlier episodes were lost, simply because BBC cleaned their archives once in a while?", Task.Status.Planning, Task.Context.Work))
        addTask(Task("Feed cat", "Download your free ebook and learn how a headless approach can boost your business in a number of ways. ", Task.Status.NextAction, Task.Context.Work))
        addTask(Task("Go shopping", "", Task.Status.Waiting))
        addTask(Task("Watch TV", "Why headless systems beat monolithic systems", Task.Status.OnHold))
        addTask(Task("Eat spaghetti", "Develop .NET, ASP.NET, .NET Core, Xamarin or Unity applications on Windows, Mac, or Linux.", Task.Status.NextAction, Task.Context.Home))
        addTask(Task("Clean garage", "", Task.Status.Planning))
        addTask(Task("Fix computer", "", Task.Status.NextAction, Task.Context.Home))
        addTask(Task("Take exam", "I think people don’t realize how empty South American countries are. Argentina is a massive country with roughly the same population as Ukraine, and only sligthly more than California (45m pop.) Peru has a population of around 32 million, 6m less than Poland and 2m more than Texas. Chile has 17 million people, the same as the Netherlands, or 2 million less than New York.", Task.Status.Waiting))
        addTask(Task("Read a book", "", Task.Status.OnHold))
        addTask(Task("Go to dentist", "", Task.Status.OnHold))

    }
}