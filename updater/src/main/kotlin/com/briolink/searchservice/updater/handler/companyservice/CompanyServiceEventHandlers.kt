package com.briolink.searchservice.updater.handler.companyservice

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.searchservice.updater.service.SyncService

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

@EventHandler("CompanyServiceSyncEvent", "1.0")
class CompanyServiceSyncEventHandler(
    private val companyServiceHandlerService: CompanyServiceHandlerService,
    syncService: SyncService,
) : SyncEventHandler<CompanyServiceSyncEvent>(ObjectSyncEnum.CompanyService, syncService) {
    override fun handle(event: CompanyServiceSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            companyServiceHandlerService.sync(objectSync)
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
