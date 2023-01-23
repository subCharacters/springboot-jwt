package com.springboot.example.springbootexemple.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Authority")
class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    var authorityName: String? = null
}