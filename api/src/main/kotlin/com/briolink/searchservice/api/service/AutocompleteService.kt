package com.briolink.searchservice.api.service

import com.briolink.lib.location.service.LocationService
import com.briolink.searchservice.common.jpa.enumeration.SearchTypeEnum
import com.briolink.searchservice.common.jpa.read.repository.SearchReadRepository
import org.springframework.stereotype.Service

@Service
class AutocompleteService(
    private val searchReadRepository: SearchReadRepository,
    private val locationService: LocationService
) {
    fun getCompanyServiceName(query: String?) =
        searchReadRepository.getAutocompleteByNameAndType(
            query?.ifBlank { null },
            SearchTypeEnum.CompanyServiceName.value
        )

    fun getSearchByNameAndTypes(query: String?, searchTypes: List<SearchTypeEnum>?) =
        searchReadRepository.getAutocompleteByNameAndTypes(query?.ifBlank { null }, searchTypes?.map { it.value })
            .map { it.name }

    fun getCompanyName(query: String?) =
        searchReadRepository.getAutocompleteByNameAndType(query?.ifBlank { null }, SearchTypeEnum.CompanyName.value)

    fun getCompanyOccupation(query: String?) =
        searchReadRepository.getAutocompleteByNameAndType(
            query?.ifBlank { null },
            SearchTypeEnum.CompanyOccupationName.value
        )

    fun getCompanyIndustry(query: String?) =
        searchReadRepository.getAutocompleteByNameAndType(
            query?.ifBlank { null },
            SearchTypeEnum.CompanyIndustryName.value
        )

    fun getCompanyRole(query: String?) =
        searchReadRepository.getAutocompleteByNameAndType(query?.ifBlank { null }, SearchTypeEnum.CompanyRoleName.value)

    fun getLocations(query: String) =
        locationService.getSuggestionLocation(query)

    fun getJobPositionTitle(query: String?) =
        searchReadRepository.getAutocompleteByNameAndType(
            query?.ifBlank { null },
            SearchTypeEnum.JobPositionTitleName.value
        )
}
