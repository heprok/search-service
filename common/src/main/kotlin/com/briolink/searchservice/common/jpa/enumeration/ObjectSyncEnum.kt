package com.briolink.searchservice.common.jpa.enumeration

enum class ObjectSyncEnum(val value: Int) {
    Company(0),
    User(1),
    CompanyService(2),
    UserJobPosition(3);

    companion object {
        private val map = values().associateBy(ObjectSyncEnum::value)
        fun fromInt(type: Int): ObjectSyncEnum = map[type]!!
    }
}
