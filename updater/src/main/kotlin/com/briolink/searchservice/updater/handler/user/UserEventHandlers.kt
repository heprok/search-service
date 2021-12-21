package com.briolink.searchservice.updater.handler.user

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler

@EventHandler("UserCreatedEvent", "1.0")
class UserEventCreatedHandler(
    private val userHandlerService: UserHandlerService,
) : IEventHandler<UserCreatedEvent> {
    override fun handle(event: UserCreatedEvent) {
        userHandlerService.create(event.data)
    }
}

@EventHandler("UserUpdatedEvent", "1.0")
class UserEventUpdatedHandler(
    private val userHandlerService: UserHandlerService
) : IEventHandler<UserUpdatedEvent> {
    override fun handle(event: UserUpdatedEvent) {
        userHandlerService.update(event.data)
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
