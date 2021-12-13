package com.briolink.searchservice.updater.handler.company

import com.briolink.searchservice.common.jpa.read.entity.CompanyIndustryReadEntity
import com.briolink.searchservice.common.jpa.read.entity.CompanyKeywordsSearch
import com.briolink.searchservice.common.jpa.read.entity.CompanyOccupationReadEntity
import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyIndustryReadRepository
import com.briolink.searchservice.common.jpa.read.repository.CompanyOccupationReadRepository
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.service.LocationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class CompanyHandlerService(
    private val companyReadRepository: CompanyReadRepository,
    private val companyOccupationReadRepository: CompanyOccupationReadRepository,
    private val companyIndustryReadRepository: CompanyIndustryReadRepository,
    private val locationService: LocationService,
) {

    fun createCompany(companyEventData: CompanyEventData): CompanyReadEntity {
        CompanyReadEntity(companyEventData.id).apply {
            name = companyEventData.name
            data = CompanyReadEntity.Data(
                website = companyEventData.website,
                slug = companyEventData.slug,
            )
            keywordsSearch = CompanyKeywordsSearch(name, "")
            return companyReadRepository.save(this)
        }
    }

    fun updateCompany(companyEventData: CompanyEventData): CompanyReadEntity {
        companyReadRepository.findById(companyEventData.id).orElseThrow { throw EntityNotFoundException("Company ${companyEventData.id} not found") } // ktlint-disable max-line-length
            .apply {
                val locationInfo = companyEventData.locationId?.let { locationService.getLocation(it) }
                val companyOccupationReadEntity = companyEventData.occupation?.let { compareAndCreateOccupation(occupationId, it.id, it.name) }
                val companyIndustryReadEntity = companyEventData.industry?.let { compareAndCreateIndustry(industryId, it.id, it.name) }
                name = companyEventData.name
                industryId = companyIndustryReadEntity?.id
                occupationId = companyOccupationReadEntity?.id
                countryId = locationInfo?.country?.id
                stateId = locationInfo?.state?.id
                cityId = locationInfo?.city?.id
                data.apply {
                    website = companyEventData.website
                    slug = companyEventData.slug
                    logo = companyEventData.logo
                    description = companyEventData.description
                    industryName = companyIndustryReadEntity?.name
                    occupationName = companyOccupationReadEntity?.name
                    location = locationInfo
                }
                keywordsSearch = CompanyKeywordsSearch(
                    companyName = name,
                    industryName = data.industryName.orEmpty(),
                    occupationName = data.occupationName.orEmpty(),
                    location = data.location?.toString().orEmpty(),
                    description = data.description.orEmpty(),
                )
                return companyReadRepository.save(this)
            }
    }

    private fun compareAndCreateOccupation(prevId: UUID?, id: UUID, name: String): CompanyOccupationReadEntity =
        if (prevId == null || prevId != id) {
            createOccupation(id, name)
        } else CompanyOccupationReadEntity(id, name)

    private fun createOccupation(id: UUID, name: String): CompanyOccupationReadEntity {
        val occupation = companyOccupationReadRepository.findById(id)
        return if (occupation.isEmpty)
            companyOccupationReadRepository.save(CompanyOccupationReadEntity(id, name))
        else occupation.get()
    }

    private fun compareAndCreateIndustry(prevId: UUID?, id: UUID, name: String): CompanyIndustryReadEntity =
        if (prevId == null || prevId != id) {
            createIndustry(id, name)
        } else CompanyIndustryReadEntity(id, name)

    private fun createIndustry(id: UUID, name: String): CompanyIndustryReadEntity {
        val industry = companyIndustryReadRepository.findById(id)
        return if (industry.isEmpty)
            companyIndustryReadRepository.save(CompanyIndustryReadEntity(id, name))
        else industry.get()
    }
}
