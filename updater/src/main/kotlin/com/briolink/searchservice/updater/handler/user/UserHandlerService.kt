package com.briolink.searchservice.updater.handler.user

import com.briolink.lib.location.model.LocationMinInfo
import com.briolink.lib.location.service.LocationService
import com.briolink.searchservice.common.jpa.enumeration.SearchTypeEnum
import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity
import com.briolink.searchservice.common.jpa.read.repository.UserReadRepository
import com.briolink.searchservice.updater.service.SearchService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserHandlerService(
    private val userReadRepository: UserReadRepository,
    private val searchService: SearchService,
    private val locationService: LocationService
) {
    fun createOrUpdate(userEventData: UserEventData): UserReadEntity {
        userReadRepository.findById(userEventData.id).orElse(
            UserReadEntity(userEventData.id).apply {
                data = UserReadEntity.Data(
                    user = UserReadEntity.User(
                        id = userEventData.id,
                        slug = userEventData.slug,
                        firstName = userEventData.firstName,
                        lastName = userEventData.lastName
                    )
                )
            }
        ).apply {
            val locationInfo = userEventData.locationId?.let { locationService.getLocationInfo(it, LocationMinInfo::class.java) }
            fullName = "${userEventData.firstName} ${userEventData.lastName}"
            countryId = locationInfo?.country?.id
            stateId = locationInfo?.state?.id
            cityId = locationInfo?.city?.id
            data.apply {
                user.firstName = userEventData.firstName
                user.lastName = userEventData.lastName
                user.description = userEventData.description
                user.image = userEventData.image
                user.slug = userEventData.slug
                user.locationInfo = locationInfo
            }
            searchService.createSearchItem(id, fullName, SearchTypeEnum.FullNameUser)
            return userReadRepository.save(this)
        }
    }

    fun updateCompany(company: CompanyReadEntity) {
        userReadRepository.updatePreviousPlaceWork(
            companyId = company.id.toString(),
            name = company.name,
            slug = company.data.slug,
            logo = company.data.logo?.toString(),
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

    fun refreshStats(userStatisticEventData: UserStatisticEventData) {
        userReadRepository.updateNumberOfVerification(
            userStatisticEventData.userId,
            userStatisticEventData.numberOfVerification
        )
    }
}
