package com.briolink.searchservice.api.service.companyservice.dto

import com.briolink.searchservice.common.dto.location.LocationId
import java.util.UUID

data class CompanyServiceFiltersDto(
    val searchText: String? = null,
    val serviceNames: List<String>? = null,
    val companyIndustryIds: List<UUID>? = null,
    val companyIds: List<UUID>? = null,
    val priceMax: Double? = null,
    val priceMin: Double? = null,
    val locationIds: List<LocationId>? = null
)
