package com.briolink.searchservice.common.jpa.projection

import java.util.UUID

interface ArrayIdNameProjection {
    val id: List<UUID>
    val name: String
}
