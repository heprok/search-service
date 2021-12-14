package com.briolink.searchservice.updater.handler.userjobposition

import com.briolink.event.Event
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.UUID

data class UserJobPositionCreatedEvent(override val data: UserJobPositionEventData) : Event<UserJobPositionEventData>("1.0")
data class UserJobPositionUpdatedEvent(override val data: UserJobPositionEventData) : Event<UserJobPositionEventData>("1.0")
data class UserJobPositionDeletedEvent(override val data: UserJobPositionDeleteEventData) : Event<UserJobPositionDeleteEventData>("1.0")

data class UserJobPositionEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val title: String,
    @JsonProperty
    val startDate: LocalDate? = null,
    @JsonProperty
    val endDate: LocalDate? = null,
    @JsonProperty
    val isCurrent: Boolean = false,
    @JsonProperty
    val companyId: UUID,
    @JsonProperty
    val userId: UUID,
)

data class UserJobPositionDeleteEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val userId: UUID,
    @JsonProperty
    val isCurrent: Boolean = false
)
