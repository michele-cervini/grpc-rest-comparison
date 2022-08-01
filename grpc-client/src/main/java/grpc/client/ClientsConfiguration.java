package grpc.client;

import grpc.client.service.SolarSystemBlockingService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.proto.solarSystem.SolarSystemServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@GrpcClientBean(
        clazz = SolarSystemServiceGrpc.SolarSystemServiceBlockingStub.class,
        beanName = "solarSystemServiceBlockingStub",
        client = @GrpcClient("solarSystemServiceBlockingClient"))
@GrpcClientBean(
        clazz = SolarSystemServiceGrpc.SolarSystemServiceStub.class,
        beanName = "solarSystemServiceStub",
        client = @GrpcClient("solarSystemServiceStreamingClient"))
public class ClientsConfiguration {

    public SolarSystemBlockingService solarSystemBlockingService(
            @Autowired SolarSystemServiceGrpc.SolarSystemServiceBlockingStub solarSystemServiceBlockingStub) {
        return new SolarSystemBlockingService(solarSystemServiceBlockingStub);
    }

//   public GreetingService greetingService(
//            @Autowired GreetingServiceGrpc.GreetingServiceBlockingStub greeetingServiceBlockingStub) {
//        return new GreetingService(greeetingServiceBlockingStub);
//   }

//    @Bean
//    public GrpcChannelConfigurer keepAliveClientConfigurer() {
//        return (channelBuilder, name) -> {
//            if (channelBuilder instanceof NettyChannelBuilder) {
//                ((NettyChannelBuilder) channelBuilder)
//                        .usePlaintext();
////                        .keepAliveTime(30, TimeUnit.SECONDS)
////                        .keepAliveTimeout(5, TimeUnit.SECONDS);
//            }
//        };
//    }
}
