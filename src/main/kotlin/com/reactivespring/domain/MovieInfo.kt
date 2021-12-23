package com.reactivespring.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Document
data class MovieInfo (
    @Id
    val movieInfoId: String?,
    @get:NotBlank(message= "MovieInfo.name must be present")
    var name: String,
    @get:NotNull
    @get:Positive(message = "MovieInfo.year must be Positive value")
    var year: Int,
    var cast: List<String>,
    var release_date: LocalDate) {
}