package com.briolink.searchservice.updater.handler.userjobposition

import com.briolink.searchservice.common.jpa.enumeration.SearchTypeEnum
import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.jpa.read.repository.UserReadRepository
import com.briolink.searchservice.updater.service.SearchService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class UserJobPositionHandlerService(
    private val companyReadRepository: CompanyReadRepository,
    private val userReadRepository: UserReadRepository,
    private val searchService: SearchService
) {
    fun create(userJobPositionEventData: UserJobPositionEventData) {
        val userReadEntity = userReadRepository.findById(userJobPositionEventData.userId)
            .orElseThrow { throw EntityNotFoundException("User with id ${userJobPositionEventData.userId} not found") }
        val companyReadEntity = companyReadRepository.findById(userJobPositionEventData.companyId)
            .orElseThrow { throw EntityNotFoundException("Company with id ${userJobPositionEventData.companyId} not found") }

        userReadEntity.apply {
            val placeOfWork = UserReadEntity.PlaceOfWork(
                companyId = companyReadEntity.id,
                userJobPositionId = userJobPositionEventData.id,
                companyName = companyReadEntity.name,
                slug = companyReadEntity.data.slug,
                logo = companyReadEntity.data.logo,
                jobPositionTitle = userJobPositionEventData.title
            )
            if (userJobPositionEventData.isCurrent) {
                industryId = companyReadEntity.industryId
                data.industryName = companyReadEntity.data.industryName
                jobPositionTitle = userJobPositionEventData.title
                currentPlaceOfWorkCompanyId = companyReadEntity.id
                data.currentPlaceOfWorkCompany = placeOfWork
            } else {
                previousPlaceOfWorkCompanyIds.add(companyReadEntity.id)
                data.previousPlaceOfWorkCompanies.add(placeOfWork)
            }
            searchService.createSearchItem(
                userJobPositionEventData.id,
                userJobPositionEventData.title,
                SearchTypeEnum.JobPositionTitleName
            )
            userReadRepository.save(this)
        }
    }

    fun update(userJobPositionEventData: UserJobPositionEventData) {
        val userReadEntity = userReadRepository.findById(userJobPositionEventData.userId)
            .orElseThrow { throw EntityNotFoundException("User with id ${userJobPositionEventData.userId} not found") }
        val companyReadEntity = companyReadRepository.findById(userJobPositionEventData.companyId)
            .orElseThrow { throw EntityNotFoundException("Company with id ${userJobPositionEventData.companyId} not found") }
        searchService.createSearchItem(
            userJobPositionEventData.id,
            userJobPositionEventData.title,
            SearchTypeEnum.JobPositionTitleName
        )
        userReadEntity.apply {
            val placeOfWork = UserReadEntity.PlaceOfWork(
                companyId = companyReadEntity.id,
                userJobPositionId = userJobPositionEventData.id,
                companyName = companyReadEntity.name,
                slug = companyReadEntity.data.slug,
                logo = companyReadEntity.data.logo,
                jobPositionTitle = userJobPositionEventData.title
            )

            if (userJobPositionEventData.isCurrent) {
                jobPositionTitle = userJobPositionEventData.title
                industryId = companyReadEntity.industryId
                data.industryName = companyReadEntity.data.industryName
                if (data.currentPlaceOfWorkCompany == null) {
                    currentPlaceOfWorkCompanyId = companyReadEntity.id
                    data.currentPlaceOfWorkCompany = placeOfWork
                } else if (data.currentPlaceOfWorkCompany!!.userJobPositionId != placeOfWork.userJobPositionId) {
                    previousPlaceOfWorkCompanyIds.add(data.currentPlaceOfWorkCompany!!.companyId)
                    data.previousPlaceOfWorkCompanies.add(data.currentPlaceOfWorkCompany!!)
                    currentPlaceOfWorkCompanyId = companyReadEntity.id
                    data.currentPlaceOfWorkCompany = placeOfWork
                }
            } else {
                data.previousPlaceOfWorkCompanies.firstOrNull { it.userJobPositionId == placeOfWork.userJobPositionId }
                    ?.also { previousPlaceWork ->
                        if (previousPlaceWork.companyId != placeOfWork.companyId) {
                            previousPlaceOfWorkCompanyIds.removeAt(previousPlaceOfWorkCompanyIds.indexOfFirst { it == previousPlaceWork.companyId }) // ktlint-disable max-line-length
                            data.previousPlaceOfWorkCompanies.remove(previousPlaceWork)
                            data.previousPlaceOfWorkCompanies.add(placeOfWork)
                            previousPlaceOfWorkCompanyIds.add(placeOfWork.companyId)
                        }
                    }
            }

            userReadRepository.save(this)
        }
    }

    fun delete(userJobPositionDeleteEventData: UserJobPositionDeleteEventData) {
        val userReadEntity = userReadRepository.findById(userJobPositionDeleteEventData.userId)
            .orElseThrow { throw EntityNotFoundException("User with id ${userJobPositionDeleteEventData.userId} not found") }
        userReadEntity.apply {
            if (userJobPositionDeleteEventData.isCurrent) {
                data.currentPlaceOfWorkCompany?.also {
                    searchService.deleteSearchItem(
                        it.userJobPositionId,
                        it.jobPositionTitle,
                        SearchTypeEnum.JobPositionTitleName
                    )
                }
                industryId = null
                data.industryName = null
                jobPositionTitle = null
                currentPlaceOfWorkCompanyId = null
                data.currentPlaceOfWorkCompany = null
            } else {
                val previousPlaceOfWorkCompany =
                    data.previousPlaceOfWorkCompanies.firstOrNull { it.userJobPositionId == userJobPositionDeleteEventData.id }
                        ?.also { placeOfWork ->
                            searchService.deleteSearchItem(
                                placeOfWork.userJobPositionId,
                                placeOfWork.jobPositionTitle,
                                SearchTypeEnum.JobPositionTitleName
                            )
                            previousPlaceOfWorkCompanyIds.removeAt(previousPlaceOfWorkCompanyIds.indexOfFirst { it == placeOfWork.companyId }) // ktlint-disable max-line-length
                            data.previousPlaceOfWorkCompanies.remove(placeOfWork)
                        }
            }
            userReadRepository.save(this)
        }
    }
}
