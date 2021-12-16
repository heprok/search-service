package com.briolink.searchservice.updater.handler.user

import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.jpa.read.repository.UserReadRepository
import com.briolink.searchservice.common.service.LocationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class UserHandlerService(
    private val companyReadRepository: CompanyReadRepository,
    private val userReadRepository: UserReadRepository,
    private val locationService: LocationService
) {
    fun create(userEventData: UserEventData): UserReadEntity {
        UserReadEntity(userEventData.id)
            .apply {
                fullName = "${userEventData.firstName} ${userEventData.lastName}"
                data = UserReadEntity.Data(
                    user = UserReadEntity.User(
                        id = userEventData.id,
                        slug = userEventData.slug,
                        firstName = userEventData.firstName,
                        lastName = userEventData.lastName
                    )
                )
                return userReadRepository.save(this)
            }
    }

    fun update(userEventData: UserEventData): UserReadEntity {
        userReadRepository.findById(userEventData.id)
            .orElseThrow { throw EntityNotFoundException("User with id ${userEventData.id} not found") }
            .apply {
                val locationInfo = userEventData.locationId?.let { locationService.getLocation(it) }
                fullName = "${userEventData.firstName} ${userEventData.lastName}"
                countryId = locationInfo?.country?.id
                stateId = locationInfo?.state?.id
                cityId = locationInfo?.city?.id
                data.apply {
                    user.firstName = userEventData.firstName
                    user.description = userEventData.description
                    user.image = userEventData.image
                    user.locationInfo = locationInfo
                }
                return userReadRepository.save(this)
            }
    }

    fun updateCompany(company: CompanyReadEntity) {
        userReadRepository.updatePreviousPlaceWork(
            companyId = company.id.toString(),
            name = company.name,
            slug = company.data.slug,
            logo = company.data.logo.toString(),
        )
        userReadRepository.updateCurrentPlaceWork(
            companyId = company.id,
            name = company.name,
            slug = company.data.slug,
            logo = company.data.logo?.toString(),
            industryId = company.industryId,
            industryName = company.data.industryName
        )
    }
}
