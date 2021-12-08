package com.briolink.searchservice.common.jpa.projection

import java.util.UUID

interface IdNameProjection {
    val id: UUID
    val name: String
}
