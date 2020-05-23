package com.levi.starwarsplanetmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PlanetDoesNotExistException extends Exception {

    public PlanetDoesNotExistException() {
        super("This planet not exist!");
    }

}
