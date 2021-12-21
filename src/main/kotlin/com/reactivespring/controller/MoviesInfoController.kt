package com.reactivespring.controller

import com.reactivespring.domain.MovieInfo
import com.reactivespring.service.MoviesInfoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("v1")
class MoviesInfoController(private val moviesInfoService: MoviesInfoService) {

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    fun addMovieInfo(@RequestBody movieInfo: MovieInfo) : Mono<MovieInfo> {

       return moviesInfoService.addMovieInfo(movieInfo)
    }

    @GetMapping("/movieinfos")
    fun getAllMovieInfos() : Flux<MovieInfo> {
        return moviesInfoService.getAllMovieInfos().log()
    }

    @GetMapping("/movieinfos/{id}")
    fun getMovieInfoById(@PathVariable id: String) : Mono<MovieInfo> {
        return moviesInfoService.getMovieInfoById(id).log()
    }

}