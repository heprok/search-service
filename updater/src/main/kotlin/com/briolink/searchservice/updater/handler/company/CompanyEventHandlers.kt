package com.briolink.searchservice.updater.handler.company

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler
import com.briolink.searchservice.updater.handler.companyservice.CompanyServiceHandlerService

@EventHandler("CompanyCreatedEvent", "1.0")
class CompanyCreatedEventHandler(
    private val companyHandlerService: CompanyHandlerService,
) : IEventHandler<CompanyCreatedEvent> {
    override fun handle(event: CompanyCreatedEvent) {
        companyHandlerService.createCompany(event.data)
    }
}

@EventHandler("CompanyUpdatedEvent", "1.0")
class CompanyUpdatedEventHandler(
    private val companyHandlerService: CompanyHandlerService,
    private val companyServiceHandlerService: CompanyServiceHandlerService,
) : IEventHandler<CompanyUpdatedEvent> {
    override fun handle(event: CompanyUpdatedEvent) {
        companyHandlerService.updateCompany(event.data).also {
            companyServiceHandlerService.updateCompany(it)
        }
    }
}
