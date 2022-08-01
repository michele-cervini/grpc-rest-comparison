package grpc.server.service;

import com.google.protobuf.Empty;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.proto.solarSystem.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@GrpcService
public class SolarSystemServiceImpl extends SolarSystemServiceGrpc.SolarSystemServiceImplBase {

    Logger log = LoggerFactory.getLogger(SolarSystemServiceImpl.class);

    @Override
    public void getSingleSolarSystem(Empty request, StreamObserver<SolarSystem> responseObserver) {

        try {
            String solarSystemString = Files.readString(Paths.get("grpc-server", "src", "main", "resources", "etc/solar-system.json"));

            SolarSystem.Builder solarSystemBuilder = SolarSystem.newBuilder();
            JsonFormat.parser().ignoringUnknownFields().merge(solarSystemString, solarSystemBuilder);

            responseObserver.onNext(solarSystemBuilder.build());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getGalaxy(Empty request, StreamObserver<Galaxy> responseObserver) {

        try {
            String galaxyString = Files.readString(Paths.get("grpc-server", "src", "main", "resources", "etc/galaxy.json"));

            Galaxy.Builder galaxyBuilder = Galaxy.newBuilder();
            JsonFormat.parser().ignoringUnknownFields().merge(galaxyString, galaxyBuilder);

            responseObserver.onNext(galaxyBuilder.build());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        responseObserver.onCompleted();
    }
}
