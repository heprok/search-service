package com.briolink.searchservice.updater.handler.company

import com.briolink.searchservice.common.jpa.enumeration.CompanyRoleTypeEnum
import com.briolink.searchservice.common.jpa.enumeration.SearchTypeEnum
import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.service.LocationService
import com.briolink.searchservice.updater.service.SearchService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class CompanyHandlerService(
    private val companyReadRepository: CompanyReadRepository,
    private val searchService: SearchService,
    private val locationService: LocationService,
) {

    fun createCompany(companyEventData: CompanyEventData): CompanyReadEntity {
        CompanyReadEntity(companyEventData.id).apply {
            searchService.createSearchItem(companyEventData.id, companyEventData.name, SearchTypeEnum.CompanyName)
            name = companyEventData.name
            data = CompanyReadEntity.Data(
                website = companyEventData.website,
                slug = companyEventData.slug,
            )
            return companyReadRepository.save(this)
        }
    }

    fun updateCompany(companyEventData: CompanyEventData): CompanyReadEntity {
        companyReadRepository.findById(companyEventData.id)
            .orElseThrow { throw EntityNotFoundException("Company ${companyEventData.id} not found") } // ktlint-disable max-line-length
            .apply {
                val locationInfo = companyEventData.locationId?.let { locationService.getLocation(it) }
                companyEventData.occupation?.also {
                    searchService.createSearchItem(it.id, it.name, SearchTypeEnum.CompanyOccupationName)
                }
                companyEventData.industry?.also {
                    searchService.createSearchItem(it.id, it.name, SearchTypeEnum.CompanyIndustryName)
                }
                searchService.createSearchItem(companyEventData.id, companyEventData.name, SearchTypeEnum.CompanyName)

                name = companyEventData.name
                industryId = companyEventData.industry?.id
                occupationId = companyEventData.occupation?.id
                countryId = locationInfo?.country?.id
                stateId = locationInfo?.state?.id
                cityId = locationInfo?.city?.id
                data.apply {
                    website = companyEventData.website
                    slug = companyEventData.slug
                    logo = companyEventData.logo
                    description = companyEventData.description
                    industryName = companyEventData.industry?.name
                    occupationName = companyEventData.occupation?.name
                    location = locationInfo
                }
                return companyReadRepository.save(this)
            }
    }

    fun refreshStats(companyStatisticEventData: CompanyStatisticEventData) {
        companyReadRepository.findById(companyStatisticEventData.companyId)
            .orElseThrow { throw EntityNotFoundException("Company ${companyStatisticEventData.companyId} not found") }
            .apply {
                numberOfVerification = companyStatisticEventData.numberOfVerification
                companyRoleIds = companyStatisticEventData.companyRoles.map { it.id }
                data.companyRoles = companyStatisticEventData.companyRoles.map {
                    searchService.createSearchItem(it.id, it.name, SearchTypeEnum.CompanyRoleName)
                    CompanyReadEntity.CompanyRole(
                        id = it.id,
                        name = it.name,
                        type = CompanyRoleTypeEnum.valueOf(it.type.name)
                    )
                }
                companyReadRepository.save(this)
            }
    }
}
