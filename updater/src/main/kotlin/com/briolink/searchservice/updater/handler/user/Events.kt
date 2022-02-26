package com.briolink.searchservice.updater.handler.user

import com.briolink.event.Event
import com.briolink.lib.location.model.LocationId
import com.briolink.lib.sync.ISyncData
import com.briolink.lib.sync.enumeration.ServiceEnum
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.util.UUID

data class UserCreatedEvent(override val data: UserEventData) : Event<UserEventData>("1.0")
data class UserUpdatedEvent(override val data: UserEventData) : Event<UserEventData>("1.0")
data class UserSyncEvent(override val data: UserEventSyncData) : Event<UserEventSyncData>("1.0")
data class UserStatisticEvent(override val data: UserStatisticEventData) : Event<UserStatisticEventData>("1.0")

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

data class UserStatisticEventData(
    @JsonProperty
    val userId: UUID,
    @JsonProperty
    val numberOfVerification: Int,
)

data class UserEventSyncData(
    override val indexObjectSync: Long,
    override val service: ServiceEnum,
    override val syncId: Int,
    override val totalObjectSync: Long,
    override val objectSync: UserEventData?,
) : ISyncData<UserEventData>
