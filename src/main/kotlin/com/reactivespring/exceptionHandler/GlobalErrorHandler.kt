package com.reactivespring.exceptionHandler

import com.reactivespring.Log
import lombok.extern.slf4j.Slf4j
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import java.util.stream.Collectors

@ControllerAdvice
class GlobalErrorHandler {
    companion object : Log();

    // for any kind of bean validation exception is caught by below function
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleRequestBodyError(ex: WebExchangeBindException) : ResponseEntity<String> {
        log.error("Exception Caught is handler is ${ex.message} \n $ex")
        val error = ex.bindingResult.allErrors.stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .sorted()
            .collect(Collectors.joining(","))
        log.error("Errors is $error")

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }
}