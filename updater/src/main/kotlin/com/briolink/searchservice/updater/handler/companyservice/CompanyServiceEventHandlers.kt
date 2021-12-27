package com.briolink.searchservice.updater.handler.companyservice

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler

@EventHandler("CompanyServiceCreatedEvent", "1.0")
class CompanyServiceCreatedEventHandler(
    private val companyServiceHandlerService: CompanyServiceHandlerService
) : IEventHandler<CompanyServiceCreatedEvent> {
    override fun handle(event: CompanyServiceCreatedEvent) {
        companyServiceHandlerService.create(event.data)
    }
}

@EventHandler("CompanyServiceUpdatedEvent", "1.0")
class CompanyServiceUpdatedEventHandler(
    private val companyServiceHandlerService: CompanyServiceHandlerService
) : IEventHandler<CompanyServiceUpdatedEvent> {
    override fun handle(event: CompanyServiceUpdatedEvent) {
        companyServiceHandlerService.update(event.data)
    }
}
@EventHandler("CompanyServiceSyncEvent", "1.0")
class CompanyServiceSyncEventHandler(
    private val companyServiceHandlerService: CompanyServiceHandlerService
) : IEventHandler<CompanyServiceSyncEvent> {
    override fun handle(event: CompanyServiceSyncEvent) {
        companyServiceHandlerService.sync(event.data)
    }
}

@EventHandler("CompanyServiceDeletedEvent", "1.0")
class CompanyServiceDeletedEventHandler(
    private val companyServiceHandlerService: CompanyServiceHandlerService,
) : IEventHandler<CompanyServiceDeletedEvent> {
    override fun handle(event: CompanyServiceDeletedEvent) {
        companyServiceHandlerService.deleteById(event.data.id)
    }
}

@EventHandler("CompanyServiceHideEvent", "1.0")
class CompanyServiceHideEventHandler(
    private val companyServiceHandlerService: CompanyServiceHandlerService,
) : IEventHandler<CompanyServiceHideEvent> {
    override fun handle(event: CompanyServiceHideEvent) {
        companyServiceHandlerService.setHidden(event.data.id, event.data.hidden)
    }
}

@EventHandler("CompanyServiceStatisticEvent", "1.0")
class CompanyServiceStatisticEventHandler(
    private val companyServiceHandlerService: CompanyServiceHandlerService
) : IEventHandler<CompanyServiceStatisticEvent> {
    override fun handle(event: CompanyServiceStatisticEvent) {
        companyServiceHandlerService.refreshStats(event.data)
    }
}
