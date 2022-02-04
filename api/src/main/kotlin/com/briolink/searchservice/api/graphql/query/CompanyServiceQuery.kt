package com.briolink.searchservice.api.graphql.query

import com.briolink.lib.location.model.LocationId
import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.graphql.fromEntity
import com.briolink.searchservice.api.service.AutocompleteService
import com.briolink.searchservice.api.service.companyservice.CompanyServiceService
import com.briolink.searchservice.api.service.companyservice.dto.CompanyServiceFiltersDto
import com.briolink.searchservice.api.service.companyservice.dto.CompanyServiceSortDto
import com.briolink.searchservice.api.service.companyservice.dto.CompanyServiceSortKeyEnum
import com.briolink.searchservice.api.types.CompanyServiceCard
import com.briolink.searchservice.api.types.CompanyServiceCardFilterParameter
import com.briolink.searchservice.api.types.CompanyServiceCardList
import com.briolink.searchservice.api.types.CompanyServiceCardSortParameter
import com.briolink.searchservice.api.types.IdNameItem
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize
import java.util.UUID

@DgsComponent
class CompanyServiceQuery(
    private val companyServiceService: CompanyServiceService,
    private val autocompleteService: AutocompleteService
) {
    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getCompanyServiceCards(
        @InputArgument filter: CompanyServiceCardFilterParameter?,
        @InputArgument sort: CompanyServiceCardSortParameter?,
        @InputArgument limit: Int,
        @InputArgument offset: Int,
    ): CompanyServiceCardList {
        val filterDto = CompanyServiceFiltersDto(
            searchText = filter?.searchText,
            serviceNames = filter?.serviceNames,
            companyIndustryIds = filter?.industryIds?.map { UUID.fromString(it) },
            companyIds = filter?.companyIds?.map { UUID.fromString(it) },
            priceMax = filter?.rangePrice?.max,
            priceMin = filter?.rangePrice?.min,
            locationIds = filter?.locationIds?.map { LocationId.fromString(it) },
        )
        val pageListCompanyServiceCard = companyServiceService.getList(
            filters = filterDto,
            sort = sort?.let {
                CompanyServiceSortDto(
                    CompanyServiceSortKeyEnum.valueOf(it.key.name),
                    direction = SortDirectionEnum.valueOf(it.direction.name)
                )
            },
            offset = offset, limit = limit
        )

        return CompanyServiceCardList(
            items = pageListCompanyServiceCard.map { CompanyServiceCard.fromEntity(it) },
            totalItems = pageListCompanyServiceCard.totalSize.toInt()
        )
    }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getCompanyName(
        @InputArgument query: String?
    ): List<IdNameItem> =
        autocompleteService.getCompanyName(query).map { IdNameItem(it.objectIds.joinToString(";"), it.name) }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getCompanyServiceName(
        @InputArgument query: String?
    ): List<String> =
        autocompleteService.getCompanyServiceName(query).map { it.name }
}
