package com.briolink.searchservice.updater.handler.userjobposition

import com.briolink.event.IEventHandler
import com.briolink.event.annotation.EventHandler

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
