package com.briolink.searchservice.updater.dataloader

import com.briolink.searchservice.common.dto.location.LocationId
import com.briolink.searchservice.common.jpa.enumeration.LocationTypeEnum
import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.jpa.read.repository.UserReadRepository
import com.briolink.searchservice.updater.handler.user.UserEventData
import com.briolink.searchservice.updater.handler.user.UserHandlerService
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URL
import java.util.UUID
import kotlin.random.Random

@Component
@Order(1)
class UserDataLoader(
    private var userReadRepository: UserReadRepository,
    private val companyReadRepository: CompanyReadRepository,
    private val userHandlerService: UserHandlerService,
) : DataLoader() {
    val listFirstName: List<String> = listOf(
        "Lynch", "Kennedy", "Williams", "Evans", "Jones", "Burton", "Miller", "Smith", "Nelson", "Lucas",
    )

    val listLastName: List<String> = listOf(
        "Scott", "Cynthia", "Thomas", "Thomas", "Lucy", "Dawn", "Jeffrey", "Ann", "Joan", "Lauren",
    )

    override fun loadData() {

        if (userReadRepository.count().toInt() == 0) {
            val mutableListUser = mutableListOf<UserReadEntity>()
            for (i in 1..COUNT_USER) {
                userHandlerService.create(
                    userEventData = UserEventData(
                        id = UUID.randomUUID(),
                        slug = listFirstName.random(),
                        firstName = listFirstName.random(),
                        lastName = listLastName.random(),
                    )
                ).also {
                    mutableListUser.add(it)
                }
            }

            mutableListUser.forEach {
                userHandlerService.update(
                    userEventData = UserEventData(
                        id = it.id,
                        slug = it.data.user.slug,
                        firstName = it.data.user.firstName,
                        lastName = it.data.user.lastName,
                        description = if (Random.nextBoolean()) null else "asdfzxcvpoqkwer",
                        locationId = LocationId(
                            Random.nextInt(1, 120), LocationTypeEnum.fromInt(Random.nextInt(0, 2))
                        ),
                        image = if (Random.nextBoolean()) URL("https://placeimg.com/148/148/people") else null,
                    )

                )
            }
        }
    }

    companion object {
        const val COUNT_USER = 30
    }
}
