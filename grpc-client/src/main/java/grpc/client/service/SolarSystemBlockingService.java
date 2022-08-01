package grpc.client.service;

import com.google.protobuf.Empty;
import lombok.AllArgsConstructor;
import org.proto.solarSystem.Galaxy;
import org.proto.solarSystem.SolarSystem;
import org.proto.solarSystem.SolarSystemServiceGrpc;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SolarSystemBlockingService {

    private SolarSystemServiceGrpc.SolarSystemServiceBlockingStub blockingStub;

    public SolarSystem getSolarSystem() {
        return blockingStub.getSingleSolarSystem(Empty.newBuilder().build());
    }

    public Galaxy getGalaxy() {
        return blockingStub.getGalaxy(Empty.newBuilder().build());
    }

}
