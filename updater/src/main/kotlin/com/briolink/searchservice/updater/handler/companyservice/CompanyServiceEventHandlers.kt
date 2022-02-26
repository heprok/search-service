package com.briolink.searchservice.updater.handler.companyservice

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.lib.sync.enumeration.UpdaterEnum
import com.briolink.lib.sync.model.SyncError
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
    private val syncService: SyncService,
) : IEventHandler<CompanyServiceSyncEvent> {
    override fun handle(event: CompanyServiceSyncEvent) {
        val syncData = event.data
        if (syncData.indexObjectSync.toInt() == 1)
            syncService.startSyncForService(syncData.syncId, syncData.service)
        if (syncData.objectSync == null) {
            syncService.completedObjectSync(syncData.syncId, syncData.service, ObjectSyncEnum.CompanyService)
            return
        }
        try {
            companyServiceHandlerService.sync(syncData.objectSync)
        } catch (ex: Exception) {
            syncService.sendSyncError(
                syncError = SyncError(
                    service = syncData.service,
                    updater = UpdaterEnum.Search,
                    syncId = syncData.syncId,
                    exception = ex,
                    indexObjectSync = syncData.indexObjectSync,
                ),
            )
        }
        if (syncData.indexObjectSync == syncData.totalObjectSync) {
            syncService.completedObjectSync(syncData.syncId, syncData.service, ObjectSyncEnum.CompanyService)
        }
    }
}
