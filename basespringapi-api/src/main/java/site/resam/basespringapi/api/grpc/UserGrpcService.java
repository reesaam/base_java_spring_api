package site.resam.basespringapi.api.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import site.resam.basespringapi.api.grpc.mapper.UserGrpcMapper;
import site.resam.basespringapi.application.identity.service.UserApplicationService;
import site.resam.basespringapi.grpc.v1.CreateUserRequest;
import site.resam.basespringapi.grpc.v1.GetUserRequest;
import site.resam.basespringapi.grpc.v1.ListUsersRequest;
import site.resam.basespringapi.grpc.v1.ListUsersResponse;
import site.resam.basespringapi.grpc.v1.UserResponse;
import site.resam.basespringapi.grpc.v1.UserServiceGrpc;

/**
 * gRPC adapter delegating to the same application service used by REST.
 */
@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserApplicationService userApplicationService;

    public UserGrpcService(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse response = UserGrpcMapper.toProto(
                userApplicationService.createUser(UserGrpcMapper.toDto(request))
        );
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse response = UserGrpcMapper.toProto(
                userApplicationService.getUser(UserGrpcMapper.toDto(request))
        );
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listUsers(ListUsersRequest request, StreamObserver<ListUsersResponse> responseObserver) {
        ListUsersResponse response = UserGrpcMapper.toProto(
                userApplicationService.listUsers(UserGrpcMapper.toDto(request))
        );
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
