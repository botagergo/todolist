package com.example.taskmanager

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "comments") var comments: String,
    @ColumnInfo(name = "status") var status: Status
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

    constructor() : this("", "", Status.None)

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Status.valueOf(parcel.readString() ?: "None")
    ) {
        uid = parcel.readInt()
    }

    @PrimaryKey(autoGenerate  = true) var uid: Int = 0

    override fun toString(): String {
        return "{title=$title,comments=${comments.substring(0, comments.length.coerceAtMost(10))},status=$status,uid=$uid}"
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.let {
            it.writeString(title)
            it.writeString(comments)
            it.writeString(status.value)
            it.writeInt(uid)
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