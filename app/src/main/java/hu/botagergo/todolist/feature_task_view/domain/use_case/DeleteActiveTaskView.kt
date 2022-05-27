package hu.botagergo.todolist.feature_task.domain.use_case

import hu.botagergo.todolist.config
import hu.botagergo.todolist.feature_task_view.data.TaskView

class DeleteActiveTaskView() {

    operator fun invoke(taskView: TaskView) {
        config.activeTaskViews.remove(taskView.uuid)
    }

    operator fun invoke(ind: Int) {
        config.activeTaskViews.removeAt(ind)
    }

}