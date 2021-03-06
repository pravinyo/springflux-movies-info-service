package com.reactivespring.controller

import com.reactivespring.domain.MovieInfo
import com.reactivespring.service.MoviesInfoService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import javax.validation.Valid

@RestController
@RequestMapping("v1")
class MoviesInfoController(private val moviesInfoService: MoviesInfoService) {

    private val moviesInfoSink = Sinks.many().replay().all<MovieInfo>()

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    fun addMovieInfo(@RequestBody @Valid movieInfo: MovieInfo): Mono<MovieInfo> {
        return moviesInfoService.addMovieInfo(movieInfo)
            .doOnNext { savedInfo -> moviesInfoSink.tryEmitNext(savedInfo) }
    }

    @PutMapping("/movieinfos/{id}")
    fun updateMovieInfo(@RequestBody updatedMovieInfo: MovieInfo, @PathVariable id: String)
            : Mono<ResponseEntity<MovieInfo>> {
        return moviesInfoService.updateMovieInfo(updatedMovieInfo, id)
            .map(ResponseEntity.ok()::body)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
            .log()
    }

    @GetMapping("/movieinfos")
    fun getAllMovieInfos(@RequestParam(value = "year", required = false) year: Int?): Flux<MovieInfo> {
        if (year != null) {
            return moviesInfoService.getAllMovieByYear(year).log()
        }
        return moviesInfoService.getAllMovieInfos().log()
    }

    @GetMapping("/movieinfos/{id}")
    fun getMovieInfoById(@PathVariable id: String): Mono<ResponseEntity<MovieInfo>> {
        return moviesInfoService.getMovieInfoById(id)
            .map { ResponseEntity.ok().body(it) }
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
            .log()
    }

    @GetMapping("/movieinfos/stream", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getMovieInfoStream(): Flux<MovieInfo> {
        return moviesInfoSink.asFlux()
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMovieById(@PathVariable id: String): Mono<Void> {
        return moviesInfoService.deleteMovieInfo(id)
    }
}