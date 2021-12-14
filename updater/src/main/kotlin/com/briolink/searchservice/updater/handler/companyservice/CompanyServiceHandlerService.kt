package com.briolink.searchservice.updater.handler.companyservice

import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.searchservice.common.jpa.read.entity.CompanyServiceKeywordsSearch
import com.briolink.searchservice.common.jpa.read.entity.CompanyServiceReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.jpa.read.repository.CompanyServiceReadRepository
import com.vladmihalcea.hibernate.type.util.ObjectMapperWrapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class CompanyServiceHandlerService(
    private val companyServiceReadRepository: CompanyServiceReadRepository,
    private val companyReadRepository: CompanyReadRepository,
) {
    fun create(companyServiceEventData: CompanyServiceEventData): CompanyServiceReadEntity {
        val companyReadEntity = companyReadRepository.findById(companyServiceEventData.companyId)
            .orElseThrow { throw EntityNotFoundException("Company with id ${companyServiceEventData.companyId} not found") }
        CompanyServiceReadEntity(companyServiceEventData.id, companyServiceEventData.companyId)
            .apply {
                name = companyServiceEventData.name
                industryId = companyReadEntity.industryId
                countryId = companyReadEntity.data.location?.country?.id
                stateId = companyReadEntity.data.location?.state?.id
                cityId = companyReadEntity.data.location?.city?.id
                data = CompanyServiceReadEntity.Data(
                    slug = companyServiceEventData.slug,
                    location = companyReadEntity.data.location,
                    industryName = companyReadEntity.data.industryName,
                    company = CompanyServiceReadEntity.Company(
                        id = companyReadEntity.id,
                        name = companyReadEntity.name,
                        slug = companyReadEntity.data.slug,
                        logo = companyReadEntity.data.logo
                    )
                )
                keywordsSearch = CompanyServiceKeywordsSearch(
                    serviceName = name,
                    companyName = data.company.name,
                    industryName = data.industryName.orEmpty(),
                    location = data.location?.toString().orEmpty(),
                )
                return companyServiceReadRepository.save(this)
            }
    }

    fun update(companyServiceEventData: CompanyServiceEventData): CompanyServiceReadEntity {
        companyServiceReadRepository.findById(companyServiceEventData.id)
            .orElseThrow { throw EntityNotFoundException("companyService with id ${companyServiceEventData.id} not found") }
            .apply {
                name = companyServiceEventData.name
                data.description = companyServiceEventData.description
                data.image = companyServiceEventData.logo
                keywordsSearch = CompanyServiceKeywordsSearch(
                    serviceName = name,
                    companyName = data.company.name,
                    industryName = data.industryName.orEmpty(),
                    location = data.location?.toString().orEmpty(),
                    description = data.description.orEmpty()
                )
                return companyServiceReadRepository.save(this)
            }
    }

    fun updateCompany(company: CompanyReadEntity) {
        companyServiceReadRepository.updateCompany(
            companyId = company.id,
            name = company.name,
            slug = company.data.slug,
            logo = company.data.logo?.toString(),
            countryId = company.countryId,
            stateId = company.stateId,
            cityId = company.cityId,
            locationJson = company.data.location?.let {
                ObjectMapperWrapper.INSTANCE.objectMapper.writeValueAsString(it)
            } ?: "null",
            location = company.data.location?.toString(),
            industryId = company.industryId,
            industryName = company.data.industryName
        )
    }

    fun deleteById(companyServiceId: UUID) {
        companyServiceReadRepository.deleteById(companyServiceId)
    }

    fun setHidden(companyServiceId: UUID, hidden: Boolean) {
        companyServiceReadRepository.setHidden(id = companyServiceId, hidden = hidden)
    }
}