package com.briolink.searchservice.api.graphql.query

import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.graphql.fromEntity
import com.briolink.searchservice.api.service.AutocompleteService
import com.briolink.searchservice.api.service.company.CompanyService
import com.briolink.searchservice.api.service.company.dto.CompanyFiltersDto
import com.briolink.searchservice.api.service.company.dto.CompanySortDto
import com.briolink.searchservice.api.service.company.dto.CompanySortKeyEnum
import com.briolink.searchservice.api.types.CompanyCard
import com.briolink.searchservice.api.types.CompanyCardFilterParameter
import com.briolink.searchservice.api.types.CompanyCardList
import com.briolink.searchservice.api.types.CompanyCardSortParameter
import com.briolink.searchservice.api.types.IdNameItem
import com.briolink.searchservice.common.dto.location.LocationId
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize
import java.util.UUID

@DgsComponent
class CompanyQuery(
    private val companyService: CompanyService,
    private val autocompleteService: AutocompleteService
) {
    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getCompanyCards(
        @InputArgument filter: CompanyCardFilterParameter?,
        @InputArgument sort: CompanyCardSortParameter?,
        @InputArgument limit: Int,
        @InputArgument offset: Int,
    ): CompanyCardList {
        val filterDto = CompanyFiltersDto(
            search = filter?.search,
            companyRoleIds = filter?.companyRoleIds?.map { UUID.fromString(it) },
            companyIndustryIds = filter?.industryIds?.map { UUID.fromString(it) },
            companyOccupationIds = filter?.occupationIds?.map { UUID.fromString(it) },
            locationIds = filter?.locationIds?.map { LocationId.fromString(it) }
        )
        val pageListCompanyCard = companyService.getList(
            filters = filterDto,
            sort = sort?.let {
                CompanySortDto(
                    CompanySortKeyEnum.valueOf(it.key.name),
                    direction = SortDirectionEnum.valueOf(it.direction.name)
                )
            },
            offset = offset, limit = limit
        )

        return CompanyCardList(
            items = pageListCompanyCard.map { CompanyCard.fromEntity(it) },
            totalItems = pageListCompanyCard.totalSize.toInt()
        )
    }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getCompanyRoles(
        @InputArgument query: String?
    ): List<IdNameItem> =
        autocompleteService.getCompanyRole(query?.ifBlank { null }).map {
            IdNameItem(id = it.id.toString(), name = it.name)
        }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getCompanyOccupations(
        @InputArgument query: String?
    ): List<IdNameItem> =
        autocompleteService.getCompanyOccupation(query).map {
            IdNameItem(id = it.id.toString(), name = it.name)
        }
}
