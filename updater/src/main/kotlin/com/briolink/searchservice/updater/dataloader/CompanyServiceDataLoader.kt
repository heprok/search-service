package com.briolink.searchservice.updater.dataloader

import com.briolink.searchservice.common.jpa.read.entity.CompanyServiceReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.common.jpa.read.repository.CompanyServiceReadRepository
import com.briolink.searchservice.updater.handler.companyservice.CompanyServiceEventData
import com.briolink.searchservice.updater.handler.companyservice.CompanyServiceHandlerService
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URL
import java.util.UUID
import kotlin.random.Random

@Component
@Order(2)
class CompanyServiceDataLoader(
    private val companyReadRepository: CompanyReadRepository,
    private val companyServiceHandlerService: CompanyServiceHandlerService,
    private val companyServiceReadRepository: CompanyServiceReadRepository
) : DataLoader() {
    val listName: List<String> = listOf(
        "Advertising on Google services",
        "Software provision",
        "Technology evalution",
        "Executive Education",
        "Online sales",
        "Product design",
        "Development",
        "Design thinking",
        "Market Assessment",
    )

    override fun loadData() {
        if (companyServiceReadRepository.count().toInt() == 0 &&
            companyReadRepository.count().toInt() != 0
        ) {
            val companyList = companyReadRepository.findAll()
            val mutableListService = mutableListOf<CompanyServiceReadEntity>()
            for (i in 1..COUNT_SERVICE) {
                val randomCompany = companyList.random()
                companyServiceHandlerService.create(
                    companyServiceEventData = CompanyServiceEventData(
                        id = UUID.randomUUID(),
                        companyId = randomCompany.id,
                        name = listName.random(),
                        slug = listName.random(),
                    )
                ).also {
                    mutableListService.add(it)
                }
            }
            mutableListService.forEach {
                companyServiceHandlerService.update(
                    companyServiceEventData = CompanyServiceEventData(
                        id = it.id,
                        companyId = it.companyId,
                        name = it.name + " /update",
                        slug = it.data.slug,
                        price = if (Random.nextBoolean()) Random.nextDouble(0.0, 50000.9) else null,
                        description = if (Random.nextBoolean()) "Description" else null,
                        logo = if (Random.nextBoolean()) URL("https://placeimg.com/640/640/tech") else null,
                    )
                )
            }
        }
    }

    companion object {
        const val COUNT_SERVICE = 100
    }
}
