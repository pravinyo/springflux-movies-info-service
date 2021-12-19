package com.reactivespring.repository

import com.reactivespring.domain.MovieInfo
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface MovieInfoRepository : ReactiveMongoRepository<MovieInfo, String> {

}