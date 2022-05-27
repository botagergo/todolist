package hu.botagergo.todolist.core.util

import java.util.*

abstract class UUIDOwner(uuid: UUID?=null) {

    val uuid: UUID = uuid ?: UUID.randomUUID()

    override fun equals(other: Any?): Boolean {
        return (other as? UUIDOwner)?.uuid == uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

}