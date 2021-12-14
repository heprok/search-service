package com.briolink.searchservice.updater.handler.user

import com.briolink.event.Event
import com.briolink.searchservice.common.dto.location.LocationId
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.util.UUID

data class UserCreatedEvent(override val data: UserEventData) : Event<UserEventData>("1.0")
data class UserUpdatedEvent(override val data: UserEventData) : Event<UserEventData>("1.0")

data class UserEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    var slug: String,
    @JsonProperty
    val firstName: String,
    @JsonProperty
    val lastName: String,
    @JsonProperty
    val description: String? = null,
    @JsonProperty
    val locationId: LocationId? = null,
    @JsonProperty
    val image: URL? = null,
)
