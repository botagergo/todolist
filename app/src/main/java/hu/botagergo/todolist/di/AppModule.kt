package hu.botagergo.todolist.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.botagergo.todolist.data.TodoListDao
import hu.botagergo.todolist.data.TodoListDatabase
import hu.botagergo.todolist.feature_task.data.repository.TaskRepositoryImpl
import hu.botagergo.todolist.feature_task_view.data.repository.TaskViewRepositoryImpl
import hu.botagergo.todolist.feature_task.domain.repository.TaskRepository
import hu.botagergo.todolist.feature_task_view.domain.TaskViewRepository
import hu.botagergo.todolist.feature_task.domain.use_case.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideTaskDao(
        @ApplicationContext context: Context
    ): TodoListDao {
        return Room.databaseBuilder(
            context, TodoListDatabase::class.java, "task"
        ).fallbackToDestructiveMigration().build().todoListDao()
    }

    @Singleton
    @Provides
    fun provideTaskRepository(
        todoListDao: TodoListDao
    ): TaskRepository {
        return TaskRepositoryImpl(todoListDao)
    }

    @Singleton
    @Provides
    fun provideTaskViewRepository(
        @ApplicationContext context: Context
    ): TaskViewRepository {
        return TaskViewRepositoryImpl(context)
    }

    @Singleton
    @Provides
    fun provideTaskUseCase(
        taskRepo: TaskRepository
    ): TaskUseCase {
        return TaskUseCase(
            getTask = GetTask(taskRepo),
            getTaskGroups = GetTaskGroups(taskRepo),
            insertTask = InsertTask(taskRepo),
            updateTask = UpdateTask(taskRepo),
            deleteTask = DeleteTask(taskRepo)
        )
    }

    @Singleton
    @Provides
    fun provideTaskViewUseCase(
        taskViewRepo: TaskViewRepository
    ): TaskViewUseCase {
        return TaskViewUseCase(
            getTaskViews = GetTaskViews(taskViewRepo),
            getActiveTaskViews = GetActiveTaskViews(taskViewRepo),
            getTaskView = GetTaskView(taskViewRepo),
            addActiveTaskView = AddActiveTaskView(),
            deleteActiveTaskView = DeleteActiveTaskView()
        )
    }

}