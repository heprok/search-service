package com.briolink.searchservice.api.graphql.query

import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.graphql.fromEntity
import com.briolink.searchservice.api.service.AutocompleteService
import com.briolink.searchservice.api.service.company.CompanyService
import com.briolink.searchservice.api.service.companyservice.CompanyServiceService
import com.briolink.searchservice.api.service.companyservice.dto.CompanyServiceSortDto
import com.briolink.searchservice.api.service.companyservice.dto.CompanyServiceSortKeyEnum
import com.briolink.searchservice.api.types.CompanyServiceCard
import com.briolink.searchservice.api.types.CompanyServiceCardList
import com.briolink.searchservice.api.types.CompanyServiceCardSortParameter
import com.briolink.searchservice.api.types.IdNameItem
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
class CompanyServiceQuery(
    private val companyServiceService: CompanyServiceService,
    private val companyService: CompanyService,
    private val autocompleteService: AutocompleteService
) {
    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getCompanyServiceCards(
        @InputArgument sort: CompanyServiceCardSortParameter? = null,
        @InputArgument limit: Int,
        @InputArgument offset: Int,
    ): CompanyServiceCardList {
        val pageListCompanyServiceCard = companyServiceService.getList(
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
        autocompleteService.getCompanyName(query).map { IdNameItem(it.id, it.name) }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getCompanyServiceName(
        @InputArgument query: String?
    ): List<IdNameItem> =
        autocompleteService.getCompanyServiceName(query).map {
            IdNameItem(it.ids.joinToString(";"), it.name)
        }
}
