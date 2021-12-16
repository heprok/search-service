package com.briolink.searchservice.updater.dataloader

import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.jpa.read.repository.UserReadRepository
import com.briolink.searchservice.updater.handler.userjobposition.UserJobPositionEventData
import com.briolink.searchservice.updater.handler.userjobposition.UserJobPositionHandlerService
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.random.Random

@Component
@Order(3)
class UserJobPositionDataLoader(
    private var userReadRepository: UserReadRepository,
    private val companyReadRepository: CompanyReadRepository,
    private val userJobPositionHandlerService: UserJobPositionHandlerService,
) : DataLoader() {

    val listName = listOf(
        "Blogger",
        "Content Manager",
        "Database administrator",
        "PC operator",
        "Programmer",
        "System administrator",
        "Web master",
        "Web programmer",
    )

    override fun loadData() {
        if (userReadRepository.count().toInt() != 0) {
            val listCompanies = companyReadRepository.findAll()
            val listUsers = userReadRepository.findAll()
            for (i in 1..COUNT_JOB_POSITION) {
                userJobPositionHandlerService.create(
                    userJobPositionEventData = UserJobPositionEventData(
                        id = UUID.randomUUID(),
                        title = listName.random(),
                        isCurrent = Random.nextBoolean(),
                        companyId = listCompanies.random().id,
                        userId = listUsers.random().id
                    )
                )
            }
        }
    }

    companion object {
        const val COUNT_JOB_POSITION = 30
    }
}
