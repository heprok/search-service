package com.briolink.searchservice.api.service.user.dto

import com.briolink.lib.location.model.LocationId
import java.util.UUID

class UserFiltersDto(
    val searchText: String? = null,
    val currentPlaceWorkCompanyIds: List<UUID>? = null,
    val previousPlaceWorkCompanyIds: List<UUID>? = null,
    val jobPositionTitles: List<String>? = null,
    val companyIndustryIds: List<UUID>? = null,
    val locationIds: List<LocationId>? = null
)
