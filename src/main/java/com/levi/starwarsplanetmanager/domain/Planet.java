package com.levi.starwarsplanetmanager.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class Planet {

    @Id
    public String id;
    public String name;
    public String climate;
    public String terrain;
    public Integer amountOfAppearanceInFilms;

    public Planet(String name, String climate, String terrain, Integer amountOfAppearanceInFilms) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.amountOfAppearanceInFilms = amountOfAppearanceInFilms;
    }
}
