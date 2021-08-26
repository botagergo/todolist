package hu.botagergo.taskmanager

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private var _tasksLiveData: MutableLiveData<ArrayList<Task>>
    private var _tasks: ArrayList<Task>
    private var _taskDao: TaskDao

    fun getTasks() : LiveData<ArrayList<Task>> {
        return _tasksLiveData
    }

    fun addTask(task: Task) {
        _taskDao.insert(task)
        _tasksLiveData.value = ArrayList(_taskDao.getAll())
    }

    fun deleteTask(task: Task) {
        _taskDao.delete(task)
        _tasksLiveData.value = ArrayList(_taskDao.getAll())
    }

    fun deleteAll() {
        _taskDao.deleteAll()
        _tasksLiveData.value = ArrayList(_taskDao.getAll())
    }

    fun updateTask(task: Task) {
        _taskDao.update(task)
        _tasksLiveData.value = ArrayList(_taskDao.getAll())
    }

    fun setSampleData() {
        deleteAll()

        addTask(Task("Go for a walk", "If you enjoy (re)watching it once in a while, you might as well do it now, because it’s getting removed from Netflix. Why? Because all Nickelodeon content is being moved to Paramount+, a new streaming service.", Task.Status.NextAction))
        addTask(Task("Take out trash", "Did you know many of the earlier episodes were lost, simply because BBC cleaned their archives once in a while?", Task.Status.Planning))
        addTask(Task("Feed cat", "Download your free ebook and learn how a headless approach can boost your business in a number of ways. ", Task.Status.NextAction))
        addTask(Task("Go shopping", "", Task.Status.Waiting))
        addTask(Task("Watch TV", "Why headless systems beat monolithic systems", Task.Status.OnHold))
        addTask(Task("Eat spaghetti", "Develop .NET, ASP.NET, .NET Core, Xamarin or Unity applications on Windows, Mac, or Linux.", Task.Status.NextAction))
        addTask(Task("Clean garage", "", Task.Status.Planning))
        addTask(Task("Fix computer", "", Task.Status.NextAction))
        addTask(Task("Take exam", "I think people don’t realize how empty South American countries are. Argentina is a massive country with roughly the same population as Ukraine, and only sligthly more than California (45m pop.) Peru has a population of around 32 million, 6m less than Poland and 2m more than Texas. Chile has 17 million people, the same as the Netherlands, or 2 million less than New York.", Task.Status.Waiting))
        addTask(Task("Read a book", "", Task.Status.OnHold))
        addTask(Task("Go to dentist", "", Task.Status.OnHold))

    }

    init {
        val db = Room.databaseBuilder(
            application, AppDatabase::class.java, "task"
        ).allowMainThreadQueries().build()

        _taskDao = db.taskDao()
        _tasks = ArrayList(_taskDao.getAll())
        _tasksLiveData = MutableLiveData(_tasks)
    }
}