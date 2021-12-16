package com.briolink.searchservice.updater.handler.userjobposition

import com.briolink.searchservice.common.jpa.read.entity.JobPositionTitleReadEntity
import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.jpa.read.repository.JobPositionTitleReadRepository
import com.briolink.searchservice.common.jpa.read.repository.UserReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class UserJobPositionHandlerService(
    private val companyReadRepository: CompanyReadRepository,
    private val userReadRepository: UserReadRepository,
    private val jobPositionTitleReadRepository: JobPositionTitleReadRepository
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
                positionTitleId = createJobPositionTittle(userJobPositionEventData.title).id
                currentPlaceOfWorkCompanyId = companyReadEntity.id
                data.currentPlaceOfWorkCompany = placeOfWork
            } else {
                previousPlaceOfWorkCompanyIds.add(companyReadEntity.id)
                data.previousPlaceOfWorkCompanies.add(placeOfWork)
            }

            userReadRepository.save(this)
        }
    }

    fun update(userJobPositionEventData: UserJobPositionEventData) {
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
                positionTitleId = createJobPositionTittle(userJobPositionEventData.title).id
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
                val previousPlaceOfWorkCompany =
                    data.previousPlaceOfWorkCompanies.first { it.userJobPositionId == placeOfWork.userJobPositionId }
                if (previousPlaceOfWorkCompany.companyId != placeOfWork.companyId) {
                    previousPlaceOfWorkCompanyIds.removeAt(previousPlaceOfWorkCompanyIds.indexOfFirst { it == previousPlaceOfWorkCompany.companyId }) // ktlint-disable max-line-length
                    data.previousPlaceOfWorkCompanies.remove(previousPlaceOfWorkCompany)
                    data.previousPlaceOfWorkCompanies.add(placeOfWork)
                    previousPlaceOfWorkCompanyIds.add(placeOfWork.companyId)
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
                industryId = null
                data.industryName = null
                positionTitleId = null
                currentPlaceOfWorkCompanyId = null
                data.currentPlaceOfWorkCompany = null
            } else {
                val previousPlaceOfWorkCompany =
                    data.previousPlaceOfWorkCompanies.first { it.userJobPositionId == userJobPositionDeleteEventData.id }
                previousPlaceOfWorkCompanyIds.removeAt(previousPlaceOfWorkCompanyIds.indexOfFirst { it == previousPlaceOfWorkCompany.companyId }) // ktlint-disable max-line-length
                data.previousPlaceOfWorkCompanies.remove(previousPlaceOfWorkCompany)
            }
            userReadRepository.save(this)
        }
    }

    private fun createJobPositionTittle(name: String): JobPositionTitleReadEntity {
        val jobPositionTitle = jobPositionTitleReadRepository.findByName(name)
        return if (jobPositionTitle.isEmpty)
            jobPositionTitleReadRepository.save(JobPositionTitleReadEntity(UUID.randomUUID(), name))
        else jobPositionTitle.get()
    }
}
