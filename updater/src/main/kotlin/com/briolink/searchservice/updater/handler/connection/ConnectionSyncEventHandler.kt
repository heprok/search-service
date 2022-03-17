package com.briolink.searchservice.updater.handler.connection

import com.briolink.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.searchservice.updater.service.SyncService
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event

@EventHandler("ConnectionSyncEvent", "1.0")
class ConnectionSyncEventHandler(
    syncService: SyncService,
) : SyncEventHandler<ConnectionSyncEvent>(ObjectSyncEnum.Connection, syncService) {
    override fun handle(event: ConnectionSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
