package com.springboot.example.springbootexemple.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*


@Entity
@Table(name = "Members")
class Members(subject: String, s1: String, authorities: Collection<GrantedAuthority?>) {
    @Id
    @JsonIgnore
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userid: Long = 0

    @Column(name = "username", length = 50, unique = true)
    var username: String? = null

    @JsonIgnore
    @Column(name = "password", length = 100)
    var password: String? = null

    @JsonIgnore
    @Column(name = "nickname", length = 50)
    var nickname: String? = null

    @JsonIgnore
    @Column(name = "activated")
    var isActivated: Boolean = false

    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_name", referencedColumnName = "authority_name")]
    )
    val authorities: Set<Authority>? = null
}