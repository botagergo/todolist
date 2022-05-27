package hu.botagergo.todolist.core.util

import androidx.databinding.ObservableList

abstract class ObservableListChangedCallback<T> :
    ObservableList.OnListChangedCallback<ObservableList<T>>() {
    override fun onItemRangeChanged(
        sender: ObservableList<T>?,
        positionStart: Int,
        itemCount: Int
    ) {
        this.onChanged(sender)
    }

    override fun onItemRangeInserted(
        sender: ObservableList<T>?,
        positionStart: Int,
        itemCount: Int
    ) {
        this.onChanged(sender)
    }

    override fun onItemRangeMoved(
        sender: ObservableList<T>?,
        fromPosition: Int,
        toPosition: Int,
        itemCount: Int
    ) {
        this.onChanged(sender)
    }

    override fun onItemRangeRemoved(
        sender: ObservableList<T>?,
        positionStart: Int,
        itemCount: Int
    ) {
        this.onChanged(sender)
    }
}