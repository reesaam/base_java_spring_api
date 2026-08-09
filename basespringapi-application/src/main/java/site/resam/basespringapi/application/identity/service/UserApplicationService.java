package site.resam.basespringapi.application.identity.service;

import site.resam.basespringapi.application.identity.dto.CreateUserRequest;
import site.resam.basespringapi.application.identity.dto.GetUserRequest;
import site.resam.basespringapi.application.identity.dto.ListUsersRequest;
import site.resam.basespringapi.application.identity.dto.ListUsersResponse;
import site.resam.basespringapi.application.identity.dto.UserResponse;
import site.resam.basespringapi.proto.annotation.ProtoRpc;
import site.resam.basespringapi.proto.annotation.ProtoService;

/**
 * Application facade shared by REST controllers and gRPC services.
 */
@ProtoService(name = "UserService")
public interface UserApplicationService {

    @ProtoRpc(name = "CreateUser")
    UserResponse createUser(CreateUserRequest request);

    @ProtoRpc(name = "GetUser")
    UserResponse getUser(GetUserRequest request);

    @ProtoRpc(name = "ListUsers")
    ListUsersResponse listUsers(ListUsersRequest request);
}
