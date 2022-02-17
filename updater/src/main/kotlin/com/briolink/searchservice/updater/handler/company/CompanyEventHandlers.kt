package com.briolink.searchservice.updater.handler.company

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler
import com.briolink.event.annotation.EventHandlers
import com.briolink.lib.sync.enumeration.UpdaterEnum
import com.briolink.lib.sync.model.SyncError
import com.briolink.searchservice.common.jpa.enumeration.ObjectSyncEnum
import com.briolink.searchservice.updater.handler.companyservice.CompanyServiceHandlerService
import com.briolink.searchservice.updater.handler.user.UserHandlerService
import com.briolink.searchservice.updater.service.SyncService

@EventHandlers(
    EventHandler("CompanyUpdatedEvent", "1.0"),
    EventHandler("CompanyCreatedEvent", "1.0")
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

@EventHandler("CompanySyncEvent", "1.0")
class CompanySyncEventHandler(
    private val companyHandlerService: CompanyHandlerService,
    private val companyServiceHandlerService: CompanyServiceHandlerService,
    private val userHandlerService: UserHandlerService,
    private val syncService: SyncService,
) : IEventHandler<CompanySyncEvent> {
    override fun handle(event: CompanySyncEvent) {
        val syncData = event.data
        if (syncData.indexObjectSync.toInt() == 1)
            syncService.startSync(syncData.syncId, syncData.service)
        try {
            companyHandlerService.createOrUpdate(syncData.objectSync).also {
                companyServiceHandlerService.updateCompany(it)
                userHandlerService.updateCompany(it)
            }
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
            syncService.completedObjectSync(syncData.syncId, syncData.service, ObjectSyncEnum.Company)
        }
    }
}
