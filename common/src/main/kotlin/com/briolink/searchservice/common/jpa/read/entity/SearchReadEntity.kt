package com.briolink.searchservice.common.jpa.read.entity

import com.briolink.searchservice.common.jpa.enumeration.SearchTypeEnum
import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "search", schema = "read")
@Entity
class SearchReadEntity(
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "name", nullable = false, length = 255)
    var name: String,
) : BaseReadEntity() {

    @Type(type = "uuid-array")
    @Column(name = "object_ids", nullable = false, columnDefinition = "uuid[]")
    var objectIds: MutableList<UUID> = mutableListOf()

    @Column(name = "type", nullable = false)
    private var _type: Int = 0

    var type: SearchTypeEnum
        get() = SearchTypeEnum.fromInt(_type)
        set(value) {
            _type = value.value
        }
}
