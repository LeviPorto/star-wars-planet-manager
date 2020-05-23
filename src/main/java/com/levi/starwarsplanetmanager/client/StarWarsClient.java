package com.levi.starwarsplanetmanager.client;

import com.levi.starwarsplanetmanager.config.RestTemplateConfig;
import com.levi.starwarsplanetmanager.dto.PlanetsDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class StarWarsClient {

    private final String STAR_WARS_BASE_URL = "https://swapi.dev/api/";

    public static final String PLANET_URL = "planets/";

    public final String PLANET_PAGE_PARAMETER = "page";

    private final RestTemplate restTemplate;
    private final RestTemplateConfig restTemplateConfig;

    public StarWarsClient(RestTemplate restTemplate, RestTemplateConfig restTemplateConfig) {
        this.restTemplate = restTemplate;
        this.restTemplateConfig = restTemplateConfig;
    }

    public HttpEntity<PlanetsDTO> getAllPlanets(String page) {
        if(page == null) {
            return restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(STAR_WARS_BASE_URL + PLANET_URL)
                    .toUriString(), HttpMethod.GET, restTemplateConfig.getDefaultHttpEntity(), PlanetsDTO.class);
        } else {
            return restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(STAR_WARS_BASE_URL + PLANET_URL)
                            .queryParam(PLANET_PAGE_PARAMETER, page).toUriString(), HttpMethod.GET,
                    restTemplateConfig.getDefaultHttpEntity(), PlanetsDTO.class);
        }
    }

}
