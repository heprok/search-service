package com.briolink.searchservice.updater.handler.connection

import com.briolink.event.Event
import com.briolink.lib.sync.ISyncData
import com.briolink.lib.sync.enumeration.ServiceEnum
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class ConnectionEventData(
    @JsonProperty
    val id: UUID
)

data class ConnectionEventSyncData(
    @JsonProperty
    override val indexObjectSync: Long,
    @JsonProperty
    override val service: ServiceEnum,
    @JsonProperty
    override val syncId: Int,
    @JsonProperty
    override val totalObjectSync: Long,
    @JsonProperty
    override val objectSync: ConnectionEventData?
) : ISyncData<ConnectionEventData>

data class ConnectionSyncEvent(override val data: ConnectionEventSyncData) : Event<ConnectionEventSyncData>("1.0")
