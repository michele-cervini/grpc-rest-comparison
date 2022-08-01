package rest.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Galaxy;
import domain.SolarSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

@RestController
public class SolarSystemController {

    Logger log = LoggerFactory.getLogger(SolarSystemController.class);
    private static SolarSystem solarSystem;
    private static Galaxy galaxy;

    // initialize the objects for get calls once and for all
    static {
        ObjectMapper mapper = new ObjectMapper();
        try {
            solarSystem = mapper.readValue(Paths.get("etc", "solar-system.json").toFile(), SolarSystem.class);
            System.out.println("Loaded solar system json");
            galaxy = mapper.readValue(Paths.get("etc", "galaxy.json").toFile(), Galaxy.class);
            System.out.println("Loaded galaxy json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/solarSystem")
    public SolarSystem getSolarSystem() {
        return solarSystem;
    }

    @PostMapping("/solarSystem")
    public void postSolarSystem(SolarSystem solarSystem) {
        log.info("Received solar system");
    }

    @GetMapping("/galaxy")
    public Galaxy getSolarSystems() {
        return galaxy;
    }

    @PostMapping("/galaxy")
    public void postGalaxy(Galaxy galaxy) {
        log.info("Received galaxy");
    }
}

