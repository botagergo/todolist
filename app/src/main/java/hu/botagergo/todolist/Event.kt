package hu.botagergo.todolist

class Event<T> {
    private val handlers: ArrayList<(T) -> Unit> = ArrayList()

    fun subscribe(handler: (T) -> Unit) {
        handlers.add(handler)
    }

    fun signal(data: T) {
        for (handler in handlers) {
            handler(data)
        }
    }
}