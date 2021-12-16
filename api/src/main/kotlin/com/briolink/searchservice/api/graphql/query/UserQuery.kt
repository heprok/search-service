package com.briolink.searchservice.api.graphql.query

import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.graphql.fromEntity
import com.briolink.searchservice.api.service.AutocompleteService
import com.briolink.searchservice.api.service.user.UserService
import com.briolink.searchservice.api.service.user.dto.UserSortDto
import com.briolink.searchservice.api.service.user.dto.UserSortKeyEnum
import com.briolink.searchservice.api.types.IdNameItem
import com.briolink.searchservice.api.types.UserCard
import com.briolink.searchservice.api.types.UserCardList
import com.briolink.searchservice.api.types.UserCardSortParameter
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
class UserQuery(
    private val userService: UserService,
    private val autocompleteService: AutocompleteService,
) {
    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getUserCards(
        @InputArgument sort: UserCardSortParameter?,
        @InputArgument limit: Int,
        @InputArgument offset: Int,
    ): UserCardList {
        val pageListUserCard = userService.getList(
            sort = sort?.let {
                UserSortDto(
                    UserSortKeyEnum.valueOf(it.key.name),
                    direction = SortDirectionEnum.valueOf(it.direction.name)
                )
            },
            offset = offset, limit = limit
        )

        return UserCardList(
            items = pageListUserCard.map { UserCard.fromEntity(it) },
            totalItems = pageListUserCard.totalSize.toInt()
        )
    }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getUserPlaceWork(
        @InputArgument query: String?
    ): List<IdNameItem> =
        autocompleteService.getCompanyName(query).map {
            IdNameItem(id = it.id, name = it.name)
        }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getUserJobPositionTitle(
        @InputArgument query: String?
    ): List<IdNameItem> =
        autocompleteService.getJobPositionTitle(query).map {
            IdNameItem(id = it.id.toString(), name = it.name)
        }
}
