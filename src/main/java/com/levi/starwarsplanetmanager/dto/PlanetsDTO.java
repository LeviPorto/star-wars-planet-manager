package com.levi.starwarsplanetmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanetsDTO {

    public List<PlanetDTO> results;
    public String next;

}
