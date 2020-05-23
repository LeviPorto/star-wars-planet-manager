package com.levi.starwarsplanetmanager.service;

import com.levi.starwarsplanetmanager.StarWarsPlanetManagerApplication;
import com.levi.starwarsplanetmanager.client.StarWarsClient;
import com.levi.starwarsplanetmanager.domain.Planet;
import com.levi.starwarsplanetmanager.dto.PlanetDTO;
import com.levi.starwarsplanetmanager.dto.PlanetsDTO;
import com.levi.starwarsplanetmanager.exception.PlanetDoesNotExistException;
import com.levi.starwarsplanetmanager.repository.PlanetRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarWarsPlanetManagerApplication.class)
public class PlanetServiceTest {

    @InjectMocks
    private PlanetService service;

    @Mock
    private PlanetRepository repository;

    @Mock
    private StarWarsClient starWarsClient;

    public static final String GEONOSIS_PLANET_NAME = "Geonosis";
    public static final String FIRST_FILM_LINK = "http://swapi.dev/api/films/6/";
    public static final String SECOND_FILM_LINK = "http://swapi.dev/api/films/7/";
    public static final String THIRD_FILM_LINK = "http://swapi.dev/api/films/8/";
    public static final String FOURTH_FILM_LINK = "http://swapi.dev/api/films/9/";
    public static final String POLIS_MASSA_PLANET_NAME = "Polis Massa";
    public static final String ARID_PLANET_CLIMATE = "arid";
    public static final String NON_EXISTENT_PLANET_NAME = "Non Existent";
    public static final String ROCK_PLANET_TERRAIN = "rock";
    public static final int PLANET_AMOUNT_OF_APPEARANCE_IN_FILMS = 3;

    @Test
    public void retrieveAllWithoutNameFilter() throws PlanetDoesNotExistException {
        BDDMockito.given(repository.findAll()).willReturn(givenAllPlanets());
        List<Planet> allPlanets = service.retrieveAll(null);
        Assert.assertEquals(allPlanets.get(0).name, GEONOSIS_PLANET_NAME);
        Assert.assertEquals(allPlanets.get(1).name, "Polis Massa");
    }

    @Test
    public void retrieveAllWithNameFilter() throws PlanetDoesNotExistException {
        BDDMockito.given(repository.findByName(GEONOSIS_PLANET_NAME)).willReturn(givenGeonosisPlanet());
        List<Planet> geonosisPlanet = service.retrieveAll(GEONOSIS_PLANET_NAME);
        Assert.assertEquals(geonosisPlanet.get(0).name, GEONOSIS_PLANET_NAME);
    }

    @Test
    public void retrieveById() throws PlanetDoesNotExistException {
        BDDMockito.given(repository.findById("4gg4g433gg3greergr")).willReturn(givenGeonosisPlanet());
        Planet geonosisPlanet = service.retrieveById("4gg4g433gg3greergr");
        Assert.assertEquals(geonosisPlanet.name, GEONOSIS_PLANET_NAME);
    }

    @Test
    public void remove() {
        service.remove("4gg4g433gg3greergr");
        BDDMockito.verify(repository, Mockito.times(1)).deleteById(anyString());
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void create() throws PlanetDoesNotExistException {
        BDDMockito.given(starWarsClient.getAllPlanets(null))
                .willReturn(new ResponseEntity(givenPlanetsFromStarWarsApi(), HttpStatus.OK));
        BDDMockito.given(repository.save(any())).willReturn(givenGeonosisPlanet().get());
        Planet geonosisPlanet = service.create(givenGeonosisPlanet().get());
        Assert.assertEquals(geonosisPlanet, givenGeonosisPlanet().get());
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void verifyFillAmountOfAppearanceInFilms() {
        BDDMockito.given(starWarsClient.getAllPlanets(null))
                .willReturn(new ResponseEntity(givenPlanetsFromStarWarsApi(), HttpStatus.OK));
        Planet geonosisPlanet = service.retrievePlanetFromStarWarsApi(givenGeonosisPlanet().get());
        Assert.assertEquals(Long.valueOf(geonosisPlanet.amountOfAppearanceInFilms), Long.valueOf(2));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void verifyPlanetExistence() {
        BDDMockito.given(starWarsClient.getAllPlanets(null))
                .willReturn(new ResponseEntity(givenPlanetsFromStarWarsApi(), HttpStatus.OK));
        Planet geonosisPlanet = service.retrievePlanetFromStarWarsApi(givenNonExistentPlanet().get());
        Assert.assertNull(geonosisPlanet);
    }

    private List<Planet> givenAllPlanets() {
        Planet geonosisPlanet = new Planet(GEONOSIS_PLANET_NAME, ARID_PLANET_CLIMATE, ROCK_PLANET_TERRAIN,
                PLANET_AMOUNT_OF_APPEARANCE_IN_FILMS);
        Planet polisMassaPlanet = new Planet(POLIS_MASSA_PLANET_NAME, ARID_PLANET_CLIMATE,
                ROCK_PLANET_TERRAIN, PLANET_AMOUNT_OF_APPEARANCE_IN_FILMS);
        return Arrays.asList(geonosisPlanet, polisMassaPlanet);
    }

    private Optional<Planet> givenGeonosisPlanet() {
        Planet geonosisPlanet = new Planet(GEONOSIS_PLANET_NAME, ARID_PLANET_CLIMATE, ROCK_PLANET_TERRAIN,
                PLANET_AMOUNT_OF_APPEARANCE_IN_FILMS);
        return Optional.of(geonosisPlanet);
    }

    private Optional<Planet> givenNonExistentPlanet() {
        Planet geonosisPlanet = new Planet(NON_EXISTENT_PLANET_NAME, ARID_PLANET_CLIMATE, ROCK_PLANET_TERRAIN, PLANET_AMOUNT_OF_APPEARANCE_IN_FILMS);
        return Optional.of(geonosisPlanet);
    }

    private PlanetsDTO givenPlanetsFromStarWarsApi() {
        PlanetDTO geonosisPlanet = new PlanetDTO(GEONOSIS_PLANET_NAME, Arrays.asList(FIRST_FILM_LINK,
                SECOND_FILM_LINK));
        PlanetDTO polisMassaPlanet = new PlanetDTO(POLIS_MASSA_PLANET_NAME, Arrays.asList(THIRD_FILM_LINK,
                FOURTH_FILM_LINK));
        return new PlanetsDTO(Arrays.asList(geonosisPlanet, polisMassaPlanet), null);
    }

}
