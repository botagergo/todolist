package hu.botagergo.todolist.feature_task.domain.use_case

import hu.botagergo.todolist.config
import hu.botagergo.todolist.feature_task_view.data.TaskView

class AddActiveTaskView() {

    operator fun invoke(taskView: TaskView) {
        config.activeTaskViews.add(taskView.uuid)
    }

    operator fun invoke(ind: Int, taskView: TaskView) {
        config.activeTaskViews.add(ind, taskView.uuid)
    }

}