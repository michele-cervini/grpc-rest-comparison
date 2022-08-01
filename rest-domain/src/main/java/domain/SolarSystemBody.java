package domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SolarSystemBody extends DomainObject {

    private String id;
    private String name;
    private String englishName;
    private Boolean isPlanet;
    private List<Moon> moons;
    private long semimajorAxis;
    private long perihelion;
    private long aphelion;
    private float eccentricity;
    private float inclination;
    private Mass mass;
    private Volume vol;
    private float density;
    private float gravity;
    private float escape;
    private float meanRadius;
    private float equaRadius;
    private float polarRadius;
    private float flattening;
    private String dimension;
    private float sideralOrbit;
    private float sideralRotation;
    private Planet aroundPlanet;
    private String discoveredBy;
    private String discoveryDate;
    private String alternativeName;
    private float axialTilt;
    private long avgTemp;
    private float mainAnomaly;
    private float argPeriapsis;
    private float longAscNode;
    private String bodyType;

}
