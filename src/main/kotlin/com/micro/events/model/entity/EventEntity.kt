package com.micro.events.model.entity

import com.micro.events.model.RemindType
import jakarta.persistence.*
import java.sql.Date

@Entity
@Table(name = "events")
data class EventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String? = null,
    var date: Date? = null,
    var notify: Boolean? = true,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val remindType: RemindType? = null,

    var description: String? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var userEntity: UserEntity? = null,
)