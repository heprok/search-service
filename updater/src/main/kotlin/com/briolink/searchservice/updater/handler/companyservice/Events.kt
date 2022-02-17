package com.briolink.searchservice.updater.handler.companyservice

import com.briolink.event.Event
import com.briolink.lib.sync.ISyncData
import com.briolink.lib.sync.enumeration.ServiceEnum
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.util.UUID

data class CompanyServiceCreatedEvent(override val data: CompanyServiceEventData) :
    Event<CompanyServiceEventData>("1.0")

data class CompanyServiceUpdatedEvent(override val data: CompanyServiceEventData) :
    Event<CompanyServiceEventData>("1.0")

data class CompanyServiceDeletedEvent(override val data: CompanyServiceDeletedData) :
    Event<CompanyServiceDeletedData>("1.0")

data class CompanyServiceHideEvent(override val data: CompanyServiceHideData) : Event<CompanyServiceHideData>("1.0")
data class CompanyServiceStatisticEvent(override val data: CompanyServiceStatisticEventData) :
    Event<CompanyServiceStatisticEventData>("1.0")

data class CompanyServiceSyncEvent(override val data: CompanyServiceSyncData) : Event<CompanyServiceSyncData>("1.0")

data class CompanyServiceEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val companyId: UUID,
    @JsonProperty
    val name: String,
    @JsonProperty
    val slug: String,
    @JsonProperty
    val price: Double? = null,
    @JsonProperty
    val description: String? = null,
    @JsonProperty
    val hidden: Boolean,
    @JsonProperty
    val logo: URL? = null,
    @JsonProperty
    val deleted: Boolean
)

data class CompanyServiceDeletedData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val companyId: UUID,
)

data class CompanyServiceHideData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val companyId: UUID,
    @JsonProperty
    val hidden: Boolean,
)

data class CompanyServiceStatisticEventData(
    @JsonProperty
    val serviceId: UUID,
    @JsonProperty
    val numberOfUses: Int
)

data class CompanyServiceSyncData(
    @JsonProperty
    override val indexObjectSync: Long,
    @JsonProperty
    override val service: ServiceEnum,
    @JsonProperty
    override val syncId: Int,
    @JsonProperty
    override val totalObjectSync: Long,
    @JsonProperty
    override val objectSync: CompanyServiceEventData
) : ISyncData<CompanyServiceEventData>
