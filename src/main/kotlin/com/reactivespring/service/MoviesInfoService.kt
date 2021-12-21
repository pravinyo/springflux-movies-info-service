package com.reactivespring.service

import com.reactivespring.domain.MovieInfo
import com.reactivespring.repository.MovieInfoRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MoviesInfoService(private val movieInfoRepository: MovieInfoRepository) {

    fun addMovieInfo(movieInfo: MovieInfo) : Mono<MovieInfo> {
        return movieInfoRepository.save(movieInfo)
    }

    fun getAllMovieInfos(): Flux<MovieInfo> {
        return movieInfoRepository.findAll()
    }

    fun getMovieInfoById(id: String): Mono<MovieInfo> {
        return movieInfoRepository.findById(id)
    }
}
