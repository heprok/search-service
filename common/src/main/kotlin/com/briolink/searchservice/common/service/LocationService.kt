package com.briolink.searchservice.common.service

import com.briolink.searchservice.common.config.AppEndpointsProperties
import com.briolink.searchservice.common.dto.location.LocationId
import com.briolink.searchservice.common.dto.location.LocationInfoDto
import com.briolink.searchservice.common.dto.location.LocationItemDto
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class LocationService(
    appEndpointsProperties: AppEndpointsProperties
) {
    val webClient = WebClient.create(appEndpointsProperties.locationservice)

    fun getLocations(query: String): List<LocationItemDto>? {
        val listLocations = webClient
            .get()
            .uri("/locations?query=$query")
            .retrieve()
            .toEntityList(LocationItemDto::class.java)
            .block()

        return listLocations?.body
    }

    fun getLocation(locationId: LocationId): LocationInfoDto? {
        return webClient
            .get()
            .uri("/location?type=${locationId.type.name}&id=${locationId.id}")
            .retrieve()
            .bodyToMono(LocationInfoDto::class.java)
            .block()
    }
}
