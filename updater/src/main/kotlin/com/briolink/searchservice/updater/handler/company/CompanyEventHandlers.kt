package com.briolink.searchservice.updater.handler.company

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler
import com.briolink.event.annotation.EventHandlers
import com.briolink.searchservice.updater.handler.companyservice.CompanyServiceHandlerService
import com.briolink.searchservice.updater.handler.user.UserHandlerService

@EventHandlers(
    EventHandler("CompanyUpdatedEvent", "1.0"),
    EventHandler("CompanyCreatedEvent", "1.0"),
    EventHandler("CompanySyncEvent", "1.0")
)
class CompanyEventHandler(
    private val companyHandlerService: CompanyHandlerService,
    private val companyServiceHandlerService: CompanyServiceHandlerService,
    private val userHandlerService: UserHandlerService
) : IEventHandler<CompanyUpdatedEvent> {
    override fun handle(event: CompanyUpdatedEvent) {
        companyHandlerService.createOrUpdate(event.data).also {
            if (event.name == "CompanyUpdatedEvent") {
                companyServiceHandlerService.updateCompany(it)
                userHandlerService.updateCompany(it)
            }
        }
    }
}

@EventHandler("CompanyStatisticEvent", "1.0")
class CompanyStatisticEventHandler(
    private val companyHandlerService: CompanyHandlerService
) : IEventHandler<CompanyStatisticEvent> {
    override fun handle(event: CompanyStatisticEvent) {
        companyHandlerService.refreshStats(event.data)
    }
}
