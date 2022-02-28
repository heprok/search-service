package com.briolink.searchservice.updater.handler.company

import com.briolink.event.Event
import com.briolink.lib.location.model.LocationId
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
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

data class CompanyCreatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")
data class CompanyUpdatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")
data class CompanyStatisticEvent(override val data: CompanyStatisticEventData) : Event<CompanyStatisticEventData>("1.0")
data class CompanySyncEvent(override val data: SyncData<CompanyEventData>) : SyncEvent<CompanyEventData>("1.0")
