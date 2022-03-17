package com.briolink.searchservice.updater.handler.userjobposition

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.searchservice.updater.service.SyncService

@EventHandler("UserJobPositionCreatedEvent", "1.0")
class UserJobPositionEventCreatedHandler(
    private val userJobPositionHandlerService: UserJobPositionHandlerService,
) : IEventHandler<UserJobPositionCreatedEvent> {
    override fun handle(event: UserJobPositionCreatedEvent) {
        if (event.data.startDate != null) userJobPositionHandlerService.create(event.data)
    }
}

@EventHandler("UserJobPositionUpdatedEvent", "1.0")
class UserJobPositionEventUpdatedHandler(
    private val userJobPositionHandlerService: UserJobPositionHandlerService
) : IEventHandler<UserJobPositionUpdatedEvent> {
    override fun handle(event: UserJobPositionUpdatedEvent) {
        if (event.data.startDate != null) userJobPositionHandlerService.update(event.data)
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
    syncService: SyncService,
) : SyncEventHandler<UserJobPositionSyncEvent>(ObjectSyncEnum.UserJobPosition, syncService) {
    override fun handle(event: UserJobPositionSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            if (objectSync.startDate != null) userJobPositionHandlerService.createOrUpdate(objectSync)
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
