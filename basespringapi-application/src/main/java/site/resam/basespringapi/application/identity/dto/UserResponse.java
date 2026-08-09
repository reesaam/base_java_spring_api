package site.resam.basespringapi.application.identity.dto;

import site.resam.basespringapi.domain.identity.UserStatus;
import site.resam.basespringapi.proto.annotation.ProtoContract;
import site.resam.basespringapi.proto.annotation.ProtoField;

@ProtoContract(name = "UserResponse")
public record UserResponse(
        @ProtoField(number = 1) String id,
        @ProtoField(number = 2) String email,
        @ProtoField(number = 3) String displayName,
        @ProtoField(number = 4) UserStatus status,
        @ProtoField(number = 5) String createdAt,
        @ProtoField(number = 6) String updatedAt,
        @ProtoField(number = 7) long version
) {
}
