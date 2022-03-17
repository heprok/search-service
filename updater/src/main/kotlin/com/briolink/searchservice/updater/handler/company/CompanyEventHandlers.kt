package com.briolink.searchservice.updater.handler.company

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
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
    syncService: SyncService,
) : SyncEventHandler<CompanySyncEvent>(ObjectSyncEnum.Company, syncService) {
    override fun handle(event: CompanySyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            companyHandlerService.createOrUpdate(objectSync).also {
                companyServiceHandlerService.updateCompany(it)
                userHandlerService.updateCompany(it)
            }
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
