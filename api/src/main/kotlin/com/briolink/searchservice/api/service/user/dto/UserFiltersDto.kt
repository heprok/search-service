package com.briolink.searchservice.api.service.user.dto

import com.briolink.searchservice.common.dto.location.LocationId
import java.util.UUID

data class UserFiltersDto(
    val search: String? = null,
    val currentPlaceWorkCompanyIds: List<UUID>? = null,
    val previousPlaceWorkCompanyIds: List<UUID>? = null,
    val jobPositionTitleIds: List<UUID>? = null,
    val companyIndustryIds: List<UUID>? = null,
    val locationIds: List<LocationId>? = null
)