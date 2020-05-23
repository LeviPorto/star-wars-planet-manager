package com.levi.starwarsplanetmanager.controller;

import com.levi.starwarsplanetmanager.exception.PlanetDoesNotExistException;
import com.levi.starwarsplanetmanager.domain.Planet;
import com.levi.starwarsplanetmanager.service.PlanetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    private final PlanetService service;

    public PlanetController(PlanetService service) {
        this.service = service;
    }

    @GetMapping
    public List<Planet> findAll(@RequestParam(value = "name", required = false) String name)
            throws PlanetDoesNotExistException {
        return service.retrieveAll(name);
    }

    @GetMapping("/{id}")
    public Planet findById(@PathVariable("id") String id) throws PlanetDoesNotExistException {
        return service.retrieveById(id);
    }

    @PostMapping
    public Planet create(@RequestBody Planet planet) throws PlanetDoesNotExistException {
        return service.create(planet);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }

}
