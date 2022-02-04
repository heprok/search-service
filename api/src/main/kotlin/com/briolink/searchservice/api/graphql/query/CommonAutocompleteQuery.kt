package com.briolink.searchservice.api.graphql.query

import com.briolink.searchservice.api.service.AutocompleteService
import com.briolink.searchservice.api.types.IdNameItem
import com.briolink.searchservice.api.types.Location
import com.briolink.searchservice.api.types.SearchTypesParameter
import com.briolink.searchservice.common.jpa.enumeration.SearchTypeEnum
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
class CommonAutocompleteQuery(
    private val autocompleteService: AutocompleteService
) {
    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getIndustries(
        @InputArgument query: String?
    ): List<IdNameItem> =
        autocompleteService.getCompanyIndustry(query).map {
            IdNameItem(id = it.objectIds.joinToString(";"), name = it.name)
        }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getLocations(
        @InputArgument query: String
    ): List<Location> =
        autocompleteService.getLocations(query)?.map {
            Location(id = it.locationId.toString(), name = it.name)
        } ?: listOf()

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getSearch(
        @InputArgument query: String?,
        @InputArgument searchTypes: SearchTypesParameter?
    ): List<String> {
        return autocompleteService.getSearchByNameAndTypes(
            query,
            searchTypes?.searchTypes?.map { SearchTypeEnum.valueOf(it.name) }
        )
    }
}
