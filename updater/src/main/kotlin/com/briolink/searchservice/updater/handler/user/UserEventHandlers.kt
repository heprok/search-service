package com.briolink.searchservice.updater.handler.user

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler
import com.briolink.event.annotation.EventHandlers

@EventHandlers(
    EventHandler("UserCreatedEvent", "1.0"),
    EventHandler("UserUpdatedEvent", "1.0"),
    EventHandler("UserSyncEvent", "1.0"),
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
