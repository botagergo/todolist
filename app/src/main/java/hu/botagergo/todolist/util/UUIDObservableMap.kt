package hu.botagergo.todolist.util

import androidx.databinding.ObservableArrayMap
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*

class UUIDObservableMap<T : UUIDOwner> : ObservableArrayMap<UUID, T>(), Serializable {
    override fun put(key: UUID?, value: T): T? {
        if (key != value.uuid) {
            throw IllegalArgumentException("Key UUID is not the same as argument")
        }

        return super.put(key, value)
    }

    fun put(value: T) {
        put(value.uuid, value)
    }

    fun putAll(values: Iterable<T>) {
        for (value in values) {
            put(value)
        }
    }

    private fun writeObject(output: ObjectOutputStream) {
        for (value in this) {
            output.writeObject(value.value)
        }
    }

    private fun readObject(input: ObjectInputStream) {
        while (input.available() > 0) {
            put(input.readObject() as T)
        }
    }

}