package hu.botagergo.todolist.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import hu.botagergo.todolist.util.EnumValueProvider
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Entity
@TypeConverters(
    LocalDateConverter::class, LocalTimeConverter::class,
    Task.Status.TaskStatusConverter::class, Task.Context.TaskContextConverter::class
)
data class Task(
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "comments") val comments: String = "",
    @ColumnInfo(name = "status") val status: Status? = null,
    @ColumnInfo(name = "context") val context: Context? = null,
    @ColumnInfo(name = "startDate") val startDate: LocalDate? = null,
    @ColumnInfo(name = "startTime") val startTime: LocalTime? = null,
    @ColumnInfo(name = "dueDate") val dueDate: LocalDate? = null,
    @ColumnInfo(name = "dueTime") val dueTime: LocalTime? = null,
    @ColumnInfo(name = "done") val done: Boolean = false,
    @PrimaryKey(autoGenerate = true) val uid: Long = 0
) : Parcelable {

    class Status(val value: String) : Serializable {

        companion object {
            lateinit var provider: EnumValueProvider
            fun values(): Array<Status> = provider.getValues().map { Status(it) }.toTypedArray()
        }

        override fun toString(): String {
            return value
        }

        object TaskStatusConverter {
            @TypeConverter
            fun toTaskStatus(status: String?): Status? {
                return if (status == null) {
                    null
                } else {
                    provider.addValue(status)
                    Status(status)
                }
            }

            @TypeConverter
            fun toStatusString(status: Status?): String? {
                return status?.value
            }
        }

        override fun equals(other: Any?): Boolean {
            return value == (other as? Status)?.value
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }

    }

    class Context(val value: String) : Serializable {

        companion object {
            lateinit var provider: EnumValueProvider
            fun values(): Array<Context> = provider.getValues().map { Context(it) }.toTypedArray()
        }

        override fun toString(): String {
            return value
        }

        object TaskContextConverter {
            @TypeConverter
            fun toTaskContext(context: String?): Context? {
                return if (context == null) {
                    null
                } else {
                    provider.addValue(context)
                    Context(context)
                }
            }

            @TypeConverter
            fun toContextString(context: Context?): String? {
                return context?.value
            }
        }

        override fun equals(other: Any?): Boolean {
            return value == (other as? Context)?.value
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }

    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readSerializable() as Status,
        parcel.readSerializable() as Context,
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
            it.writeSerializable(status)
            it.writeSerializable(context)
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