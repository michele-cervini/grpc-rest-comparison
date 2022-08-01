package grpc.client;

import grpc.client.service.SolarSystemBlockingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

@SpringBootApplication
public class Application {

    Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(
            @Autowired SolarSystemBlockingService solarSystemBlockingService) {
        return args -> {

            Instant start = Instant.now();
            solarSystemBlockingService.getSolarSystem();
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();

            log.info("solarSystemBlockingService.getSolarSystem() - Duration (ms): {}", timeElapsed);



            start = Instant.now();
            solarSystemBlockingService.getGalaxy();
            finish = Instant.now();
            timeElapsed = Duration.between(start, finish).toMillis();

            log.info("solarSystemBlockingService.getGalaxy() - Duration (ms): {}", timeElapsed);

        };
    }
}
