package com.briolink.searchservice.updater.handler.company

import com.briolink.event.Event
import com.briolink.searchservice.common.dto.location.LocationId
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

data class CompanyCreatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")
data class CompanyUpdatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")