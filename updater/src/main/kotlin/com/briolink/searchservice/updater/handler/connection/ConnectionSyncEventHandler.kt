package com.briolink.searchservice.updater.handler.connection

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler
import com.briolink.lib.sync.enumeration.ServiceEnum
import com.briolink.searchservice.updater.service.SyncService

@EventHandler("ConnectionSyncEvent", "1.0")
class ConnectionSyncEventHandler(
    private val syncService: SyncService,
) : IEventHandler<ConnectionSyncEvent> {
    override fun handle(event: ConnectionSyncEvent) {
        val syncData = event.data
        if (syncData.indexObjectSync.toInt() == 1)
            syncService.syncWebClient.sendCompletedSyncAtUpdater(syncService.CURRENT_UPDATER, ServiceEnum.Connection)
    }
}
