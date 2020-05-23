package com.levi.starwarsplanetmanager.service;

import com.levi.starwarsplanetmanager.client.StarWarsClient;
import com.levi.starwarsplanetmanager.dto.PlanetDTO;
import com.levi.starwarsplanetmanager.dto.PlanetsDTO;
import com.levi.starwarsplanetmanager.exception.PlanetDoesNotExistException;
import com.levi.starwarsplanetmanager.domain.Planet;
import com.levi.starwarsplanetmanager.repository.PlanetRepository;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {

    private final PlanetRepository repository;
    private final StarWarsClient starWarsClient;

    public PlanetService(PlanetRepository repository, StarWarsClient starWarsClient) {
        this.repository = repository;
        this.starWarsClient = starWarsClient;
    }

    public List<Planet> retrieveAll(String name) throws PlanetDoesNotExistException {
        if(name == null ) {
            return repository.findAll();
        } else {
            return retrieveByName(name);
        }

    }

    public Planet retrieveById(String id) throws PlanetDoesNotExistException {
        Optional<Planet> planet = repository.findById(id);
        if(planet.isPresent()) {
            return planet.get();
        } else {
            throw new PlanetDoesNotExistException();
        }
    }

    public Planet create(Planet planet) throws PlanetDoesNotExistException {
        Planet planetDTO = retrievePlanetFromStarWarsApi(planet);
        if (planetDTO != null) return repository.save(planetDTO);
        throw new PlanetDoesNotExistException();
    }

    public void remove(String id) {
        repository.deleteById(id);
    }

    private List<Planet> retrieveByName(String name) throws PlanetDoesNotExistException {
        Optional<Planet> planet = repository.findByName(name);
        if (planet.isPresent()) {
            return Collections.singletonList(planet.get());
        } else {
            throw new PlanetDoesNotExistException();
        }
    }

    public Planet retrievePlanetFromStarWarsApi(Planet planet) {
        boolean existMorePlanetToVerifyOnApi = true;
        boolean firstRequest = true;
        int page = 1;

        while(existMorePlanetToVerifyOnApi) {
            PlanetsDTO planetsDTO = getAllPlanetsBasedOnPageRequest(firstRequest, page).getBody();
            Planet planetDTO = getPlanetIfExist(planet, planetsDTO);
            if (planetDTO != null) {
                return planetDTO;
            }
            if (planetsDTO.next == null) {
                existMorePlanetToVerifyOnApi = false;
            }
            firstRequest = false;
            page++;
        }

        return null;
    }

    private Planet getPlanetIfExist(Planet planet, PlanetsDTO planetsDTO) {
        List<PlanetDTO> planetDTOS = planetsDTO.results;
        for (PlanetDTO planetDTO : planetDTOS) {
            if (planet.name.equals(planetDTO.name)) {
                return new Planet(planet.name, planet.climate, planet.terrain, planetDTO.films.size());
            }
        }
        return null;
    }

    private HttpEntity<PlanetsDTO> getAllPlanetsBasedOnPageRequest(boolean firstQuery, int page) {
        HttpEntity<PlanetsDTO> response;
        if (firstQuery) {
            response = starWarsClient.getAllPlanets(null);
        } else {
            response = starWarsClient.getAllPlanets(String.valueOf(page));
        }
        return response;
    }

}
