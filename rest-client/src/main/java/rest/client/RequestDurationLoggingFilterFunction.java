package rest.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

public class RequestDurationLoggingFilterFunction implements ExchangeFilterFunction {

    Logger log = LoggerFactory.getLogger(RequestDurationLoggingFilterFunction.class);
    private static final String START_TIME = RequestDurationLoggingFilterFunction.class.getName() + ".START_TIME";

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request).doOnEach((signal) -> {
            if (!signal.isOnComplete()) {
                long duration = Duration.between(signal.getContextView().get(START_TIME), Instant.now()).toMillis();
                log.info("Rest call: {} ms", duration);
            }
        }).contextWrite(ctx -> ctx.put(START_TIME, Instant.now()));
    }
}