package com.briolink.searchservice.updater.handler.userjobposition

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.lib.sync.enumeration.UpdaterEnum
import com.briolink.lib.sync.model.SyncError
import com.briolink.searchservice.updater.service.SyncService

@EventHandler("UserJobPositionCreatedEvent", "1.0")
class UserJobPositionEventCreatedHandler(
    private val userJobPositionHandlerService: UserJobPositionHandlerService,
) : IEventHandler<UserJobPositionCreatedEvent> {
    override fun handle(event: UserJobPositionCreatedEvent) {
        userJobPositionHandlerService.create(event.data)
    }
}

@EventHandler("UserJobPositionUpdatedEvent", "1.0")
class UserJobPositionEventUpdatedHandler(
    private val userJobPositionHandlerService: UserJobPositionHandlerService
) : IEventHandler<UserJobPositionUpdatedEvent> {
    override fun handle(event: UserJobPositionUpdatedEvent) {
        userJobPositionHandlerService.update(event.data)
    }
}

@EventHandler("UserJobPositionDeletedEvent", "1.0")
class UserJobPositionEventDeletedHandler(
    private val userJobPositionHandlerService: UserJobPositionHandlerService
) : IEventHandler<UserJobPositionDeletedEvent> {
    override fun handle(event: UserJobPositionDeletedEvent) {
        userJobPositionHandlerService.delete(event.data)
    }
}

@EventHandler("UserJobPositionSyncEvent", "1.0")
class UserJobPositionSyncEventHandler(
    private val userJobPositionHandlerService: UserJobPositionHandlerService,
    private val syncService: SyncService,
) : IEventHandler<UserJobPositionSyncEvent> {
    override fun handle(event: UserJobPositionSyncEvent) {
        val syncData = event.data
        if (syncData.indexObjectSync.toInt() == 1)
            syncService.startSyncForService(syncData.syncId, syncData.service)
        if (syncData.objectSync == null) {
            syncService.completedObjectSync(syncData.syncId, syncData.service, ObjectSyncEnum.UserJobPosition)
            return
        }
        try {
            userJobPositionHandlerService.update(syncData.objectSync)
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
        if (syncData.indexObjectSync == syncData.totalObjectSync)
            syncService.completedObjectSync(syncData.syncId, syncData.service, ObjectSyncEnum.UserJobPosition)
    }
}
