package rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Galaxy;
import domain.SolarSystem;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

@SpringBootApplication
public class Application {

    private static final String SOLAR_SYSTEM_REST_ENDPOINT = "https://localhost:8443/rest/solarSystem";
    private static final String GALAXY_REST_ENDPOINT = "https://localhost:8443/rest/galaxy";
    private static final int NUMBER_OF_CALLS = 100;
    private static SolarSystem solarSystem;
    private static Galaxy galaxy;

    Logger log = LoggerFactory.getLogger(Application.class);

    // initialize the objects for get calls once and for all
    static {
        ObjectMapper mapper = new ObjectMapper();
        try {
            solarSystem = mapper.readValue(Paths.get("etc", "solar-system.json").toFile(), SolarSystem.class);
            galaxy = mapper.readValue(Paths.get("etc", "galaxy.json").toFile(), Galaxy.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RequestDurationLoggingFilterFunction requestDurationLoggingFilterFunction() {
        return new RequestDurationLoggingFilterFunction();
    }

    @Bean
    public WebClient createWebClient(RequestDurationLoggingFilterFunction requestDurationLoggingFilterFunction) throws SSLException {

        // Disable certificate validation
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

        // Increase default max json body
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 100000)).build();

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .filter(requestDurationLoggingFilterFunction)
                .build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException, KeyManagementException {

        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return builder.requestFactory(() -> requestFactory).build();

    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate, WebClient webClient) {
        return args -> {

//            log.info("---------------- Start getSolarSystem (parallel)----------------");
//            IntStream.range(0, NUMBER_OF_CALLS).parallel().forEach(n -> getSolarSystem(webClient).subscribe());

//            log.info("---------------- Start getSolarSystem (async)----------------");
//            IntStream.range(0, NUMBER_OF_CALLS).forEach(n -> getSolarSystemAsync(webClient).subscribe());

            log.info("---------------- Start getSolarSystem (sync)----------------");
            IntStream.range(0, NUMBER_OF_CALLS).forEach(n -> getSolarSystemSync(webClient));

            log.info("---------------- Start getGalaxy (sync)----------------");
            IntStream.range(0, NUMBER_OF_CALLS).forEach(n -> getGalaxySync(webClient));

//            for(int i = 0; i < NUMBER_OF_CALLS; i++) {
//                webClient.get()
//                        .uri(SOLAR_SYSTEM_REST_ENDPOINT)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .retrieve()
//                        .bodyToMono(SolarSystem.class)
//                        .subscribe();
//            }


//            webClient.get()
//                    .uri(GALAXY_REST_ENDPOINT)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .retrieve()
//                    .bodyToFlux(Galaxy.class)
//                    .subscribe(galaxy -> System.out.println(galaxy));



//            log.info("---------------- Start getSolarSystem calls ----------------");
//            for(int i = 0; i < NUMBER_OF_CALLS; i++) {
//                Instant start = Instant.now();
//                restTemplate.getForEntity(
//                        SOLAR_SYSTEM_REST_ENDPOINT, SolarSystem.class);
//                Instant finish = Instant.now();
//                long timeElapsed = Duration.between(start, finish).toMillis();
//                log.info("RestService -> getSolarSystem() - Duration (ms): {}", timeElapsed);
//            }
//
//            log.info("---------------- Start getGalaxy calls ----------------");
//            for(int i = 0; i < NUMBER_OF_CALLS; i++) {
//
//                Instant start = Instant.now();
//                restTemplate.getForEntity(
//                        GALAXY_REST_ENDPOINT, Galaxy.class);
//                Instant finish = Instant.now();
//                long timeElapsed = Duration.between(start, finish).toMillis();
//                log.info("RestService -> getGalaxy() - Duration (ms): {}", timeElapsed);
//            }
//
//            log.info("---------------- Start postSolarSystem calls ----------------");
//            for(int i = 0; i < NUMBER_OF_CALLS; i++) {
//                Instant start = Instant.now();
//                restTemplate.postForObject(SOLAR_SYSTEM_REST_ENDPOINT, solarSystem, Void.class);
//                Instant finish = Instant.now();
//                long timeElapsed = Duration.between(start, finish).toMillis();
//                log.info("RestService -> postSolarSystem() - Duration (ms): {}", timeElapsed);
//            }
//
//            log.info("---------------- Start postGalaxy calls ----------------");
//            for(int i = 0; i < NUMBER_OF_CALLS; i++) {
//
//                Instant start = Instant.now();
//                restTemplate.postForObject(GALAXY_REST_ENDPOINT, galaxy, Void.class);
//                Instant finish = Instant.now();
//                long timeElapsed = Duration.between(start, finish).toMillis();
//                log.info("RestService -> postGalaxy() - Duration (ms): {}", timeElapsed);
//            }

        };
    }

    public Mono<SolarSystem> getSolarSystemAsync(WebClient webClient) {
        return webClient.get()
                .uri(SOLAR_SYSTEM_REST_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SolarSystem.class);
    }

    public SolarSystem getSolarSystemSync(WebClient webClient) {
        Instant start = Instant.now();
        SolarSystem solarSystem = webClient.get()
                .uri(SOLAR_SYSTEM_REST_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SolarSystem.class)
                .block();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("With json umarshalling: {} ms", timeElapsed);
        return solarSystem;
    }

    public Galaxy getGalaxySync(WebClient webClient) {
        Instant start = Instant.now();
        Galaxy galaxy = webClient.get()
                .uri(GALAXY_REST_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Galaxy.class)
                .block();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Rest call with unmarshalling duration: {} ms", timeElapsed);
        return galaxy;
    }

}
