package com.reactivespring.repository

import com.reactivespring.domain.MovieInfo
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface MovieInfoRepository : ReactiveMongoRepository<MovieInfo, String> {

    fun findByYear(year: Int) : Flux<MovieInfo>
}