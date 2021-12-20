package com.briolink.searchservice.api.service.company.dto

import com.briolink.searchservice.common.dto.location.LocationId
import java.util.UUID

class CompanyFiltersDto(
    val searchText: String? = null,
    val companyRoleIds: List<UUID>? = null,
    val companyIndustryIds: List<UUID>? = null,
    val companyOccupationIds: List<UUID>? = null,
    val locationIds: List<LocationId>? = null
)
