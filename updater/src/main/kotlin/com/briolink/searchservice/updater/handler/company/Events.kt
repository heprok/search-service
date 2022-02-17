package com.briolink.searchservice.updater.handler.company

import com.briolink.event.Event
import com.briolink.lib.location.model.LocationId
import com.briolink.lib.sync.ISyncData
import com.briolink.lib.sync.enumeration.ServiceEnum
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.util.UUID

data class CompanyEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String,
    @JsonProperty
    val slug: String,
    @JsonProperty
    val logo: URL? = null,
    @JsonProperty
    val website: URL? = null,
    @JsonProperty
    val description: String? = null,
    @JsonProperty
    val locationId: LocationId? = null,
    @JsonProperty
    val industry: CompanyIndustryData? = null,
    @JsonProperty
    val occupation: CompanyOccupationData? = null,
)

data class CompanyIndustryData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String,
)

data class CompanyOccupationData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String
)

data class CompanyStatisticEventData(
    @JsonProperty
    val companyId: UUID,
    @JsonProperty
    val numberOfVerifications: Int,
    @JsonProperty
    val companyRoles: ArrayList<ConnectionCompanyRoleData>
)

data class ConnectionCompanyRoleData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String,
    @JsonProperty
    val type: ConnectionCompanyRoleType
)

enum class ConnectionCompanyRoleType(val value: Int) {
    @JsonProperty("0")
    Buyer(0),

    @JsonProperty("1")
    Seller(1)
}

data class CompanyEventSyncData(
    @JsonProperty
    override val indexObjectSync: Long,
    @JsonProperty
    override val service: ServiceEnum,
    @JsonProperty
    override val syncId: Int,
    @JsonProperty
    override val totalObjectSync: Long,
    @JsonProperty
    override val objectSync: CompanyEventData
) : ISyncData<CompanyEventData>

data class CompanyCreatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")
data class CompanyUpdatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")
data class CompanyStatisticEvent(override val data: CompanyStatisticEventData) : Event<CompanyStatisticEventData>("1.0")
data class CompanySyncEvent(override val data: CompanyEventSyncData) : Event<CompanyEventSyncData>("1.0")
