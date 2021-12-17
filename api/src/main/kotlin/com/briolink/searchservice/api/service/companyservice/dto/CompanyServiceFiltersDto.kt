package com.briolink.searchservice.api.service.companyservice.dto

import com.briolink.searchservice.common.dto.location.LocationId
import java.util.UUID

data class CompanyServiceFiltersDto(
    val search: String? = null,
    val serviceIds: List<UUID>? = null,
    val companyIndustryIds: List<UUID>? = null,
    val companyIds: List<UUID>? = null,
    val priceMax: Double? = null,
    val priceMin: Double? = null,
    val locationIds: List<LocationId>? = null
)
