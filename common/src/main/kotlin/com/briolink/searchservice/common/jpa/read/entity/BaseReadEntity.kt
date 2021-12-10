package com.briolink.searchservice.common.jpa.read.entity

import com.vladmihalcea.hibernate.type.array.ListArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import javax.persistence.MappedSuperclass

@TypeDefs(
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class),
    TypeDef(name = "uuid-array", typeClass = ListArrayType::class)
)
@MappedSuperclass
abstract class BaseReadEntity
