package hu.botagergo.todolist.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalTime

@Entity
@TypeConverters(LocalDateConverter::class, LocalTimeConverter::class)
data class Task(
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "comments") val comments: String = "",
    @ColumnInfo(name = "status") val status: Status = Status.None,
    @ColumnInfo(name = "context") val context: Context = Context.None,
    @ColumnInfo(name = "startDate") val startDate: LocalDate? = null,
    @ColumnInfo(name = "startTime") val startTime: LocalTime? = null,
    @ColumnInfo(name = "dueDate") val dueDate: LocalDate? = null,
    @ColumnInfo(name = "dueTime") val dueTime: LocalTime? = null,
    @ColumnInfo(name = "done") val done: Boolean = false,
    @PrimaryKey(autoGenerate = true) val uid: Long = 0
) : Parcelable {

    enum class Status(val value: String) {
        None("None"),
        NextAction("Next Action"),
        Waiting("Waiting"),
        Planning("Planning"),
        OnHold("On Hold");

        override fun toString(): String {
            return value
        }
    }

    enum class Context(val value: String) {
        None("None"),
        Home("Home"),
        Work("Work"),
        Errands("Errands");

        override fun toString(): String {
            return value
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Status.valueOf(parcel.readString() ?: "None"),
        Context.valueOf(parcel.readString() ?: "None"),
        parcel.readSerializable() as LocalDate,
        parcel.readSerializable() as LocalTime,
        parcel.readSerializable() as LocalDate,
        parcel.readSerializable() as LocalTime,
        parcel.readBoolean(),
        parcel.readLong()
    )

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.let {
            it.writeString(title)
            it.writeString(comments)
            it.writeString(status.value)
            it.writeString(context.value)
            it.writeSerializable(startDate)
            it.writeSerializable(startTime)
            it.writeSerializable(dueDate)
            it.writeSerializable(dueTime)
            it.writeBoolean(done)
            it.writeLong(uid)
        }
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}