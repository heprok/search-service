package com.briolink.searchservice.updater.handler.connection

import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class ConnectionEventData(
    @JsonProperty
    val id: UUID
)

data class ConnectionSyncEvent(override val data: SyncData<ConnectionEventData>) : SyncEvent<ConnectionEventData>("1.0")
