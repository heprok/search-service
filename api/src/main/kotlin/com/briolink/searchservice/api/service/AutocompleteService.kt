package com.briolink.searchservice.api.service

import com.briolink.searchservice.common.jpa.read.repository.CompanyIndustryReadRepository
import com.briolink.searchservice.common.jpa.read.repository.CompanyOccupationReadRepository
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.jpa.read.repository.CompanyRoleReadRepository
import com.briolink.searchservice.common.jpa.read.repository.CompanyServiceReadRepository
import com.briolink.searchservice.common.jpa.read.repository.JobPositionTitleReadRepository
import com.briolink.searchservice.common.service.LocationService
import org.springframework.stereotype.Service

@Service
class AutocompleteService(
    private val companyReadRepository: CompanyReadRepository,
    private val companyServiceReadRepository: CompanyServiceReadRepository,
    private val companyOccupationReadRepository: CompanyOccupationReadRepository,
    private val companyIndustryReadRepository: CompanyIndustryReadRepository,
    private val companyRoleReadRepository: CompanyRoleReadRepository,
    private val josPositionTitleReadRepository: JobPositionTitleReadRepository,
    private val locationService: LocationService
) {
    fun getCompanyServiceName(query: String?) =
        companyServiceReadRepository.getAutocompleteByName(query?.ifBlank { null })

    fun getCompanyName(query: String?) =
        companyReadRepository.getAutocompleteByName(query?.ifBlank { null })

    fun getCompanyOccupation(query: String?) =
        companyOccupationReadRepository.getAutocompleteByName(query?.ifBlank { null })

    fun getCompanyIndustry(query: String?) =
        companyIndustryReadRepository.getAutocompleteByName(query?.ifBlank { null })

    fun getCompanyRole(query: String?) =
        companyRoleReadRepository.getAutocompleteByName(query?.ifBlank { null })

    fun getLocations(query: String) =
        locationService.getLocations(query)

    fun getJobPositionTitle(query: String?) =
        josPositionTitleReadRepository.getAutocompleteByName(query)
}
