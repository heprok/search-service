package com.briolink.searchservice.updater.handler.user

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler
import com.briolink.event.annotation.EventHandlers
import com.briolink.lib.sync.enumeration.UpdaterEnum
import com.briolink.lib.sync.model.SyncError
import com.briolink.searchservice.common.jpa.enumeration.ObjectSyncEnum
import com.briolink.searchservice.updater.service.SyncService

@EventHandlers(
    EventHandler("UserCreatedEvent", "1.0"),
    EventHandler("UserUpdatedEvent", "1.0"),
)
class UserEventSyncHandler(
    private val userHandlerService: UserHandlerService,
) : IEventHandler<UserCreatedEvent> {
    override fun handle(event: UserCreatedEvent) {
        userHandlerService.createOrUpdate(event.data)
    }
}

@EventHandler("UserStatisticEvent", "1.0")
class UserStatisticEventHandler(
    private val userHandlerService: UserHandlerService
) : IEventHandler<UserStatisticEvent> {
    override fun handle(event: UserStatisticEvent) {
        userHandlerService.refreshStats(event.data)
    }
}

@EventHandler("UserSyncEvent", "1.0")
class UserSyncEventHandler(
    private val userHandlerService: UserHandlerService,
    private val syncService: SyncService,
) : IEventHandler<UserSyncEvent> {
    override fun handle(event: UserSyncEvent) {
        val syncData = event.data
        if (syncData.indexObjectSync.toInt() == 1)
            syncService.startSync(syncData.syncId, syncData.service)
        try {
            userHandlerService.createOrUpdate(syncData.objectSync)
        } catch (ex: Exception) {
            syncService.sendSyncError(
                syncError = SyncError(
                    service = syncData.service,
                    updater = UpdaterEnum.Search,
                    syncId = syncData.syncId,
                    exception = ex,
                    indexObjectSync = syncData.indexObjectSync
                )
            )
        }
        if (syncData.indexObjectSync == syncData.totalObjectSync)
            syncService.completedObjectSync(syncData.syncId, syncData.service, ObjectSyncEnum.User)
    }
}
