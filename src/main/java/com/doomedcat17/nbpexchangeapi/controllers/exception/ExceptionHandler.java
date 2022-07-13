package com.doomedcat17.nbpexchangeapi.controllers.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@RestControllerAdvice
public class ExceptionHandler implements ProblemHandling {

}
